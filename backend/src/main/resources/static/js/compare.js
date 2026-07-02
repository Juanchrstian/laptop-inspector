async function loadCompare() {

    const params =
        new URLSearchParams(
            window.location.search
        );

    const cycleId =
        params.get("cycleId");

    if (!cycleId) {

        console.error(
            "cycleId not found"
        );

        return;
    }

    document.getElementById(
        "serialTitle"
    ).innerText =
        "Comparison Result";

    const response =
        await fetch(
            `/api/device-checks/compare/${cycleId}`
        );

    const data =
        await response.json();

    let before = null;
    let after = null;

    data.forEach(item => {

        if (
            item.checkType
                ?.toUpperCase()
            === "BEFORE"
        ) {

            before = item;
        }

        if (
            item.checkType
                ?.toUpperCase()
            === "AFTER"
        ) {

            after = item;
        }
    });

    renderInfo(
        before,
        after
    );

    renderCompare(
        before,
        after
    );

    renderSummary(
        before,
        after
    );

    renderRecommendation(
        before,
        after
    );

    initExportButton(
        cycleId
    );
}

// =========================================
// INFO
// =========================================

function renderInfo(
        before,
        after
) {

    document.getElementById(
        "borrowerText"
    ).innerText =
        before?.borrowerName || "-";

    document.getElementById(
        "beforeInspectorText"
    ).innerText =
        before?.inspector || "-";

    document.getElementById(
        "afterInspectorText"
    ).innerText =
        after?.inspector || "-";

    document.getElementById(
        "beforeTimeText"
    ).innerText =
        formatDateTime(
            before?.submittedAt
        );

    document.getElementById(
        "afterTimeText"
    ).innerText =
        formatDateTime(
            after?.submittedAt
        );
}

// =========================================
// COMPARE
// =========================================

function renderCompare(
        before,
        after
) {

    const tbody =
        document.getElementById(
            "compareBody"
        );

    tbody.innerHTML = "";

    if (!before || !after) {

        tbody.innerHTML =

            `
            <tr>
                <td colspan="4">
                    Comparison data incomplete
                </td>
            </tr>
            `;

        return;
    }

    addRow(
        tbody,
        "CPU",
        before.cpu,
        after.cpu
    );

    addRow(
        tbody,
        "GPU",
        before.gpu,
        after.gpu
    );

    addRow(
        tbody,
        "RAM",
        formatGB(before.totalRam),
        formatGB(after.totalRam)
    );

    addRow(
        tbody,
        "Total Storage",
        formatGB(before.totalStorage),
        formatGB(after.totalStorage)
    );

    addRow(
        tbody,
        "Available Storage",
        formatGB(before.availableStorage),
        formatGB(after.availableStorage)
    );

    addRow(
        tbody,
        "Battery Health",
        formatBattery(before.batteryHealth),
        formatBattery(after.batteryHealth)
    );
}

// =========================================
// SUMMARY
// =========================================

function renderSummary(
        before,
        after
) {

    const summary =
        document.getElementById(
            "summaryText"
        );

    if (!before || !after) {

        summary.innerHTML =
            "Data perbandingan belum lengkap.";

        return;
    }

    let html = "";

    const storageDiff =
        before.availableStorage
        -
        after.availableStorage;

    const storagePercent =
        (
            storageDiff
            /
            before.availableStorage
        ) * 100;

    if (storageDiff > 0) {

        html +=

            `<p>
            Penyimpanan tersedia berkurang
            ${storagePercent.toFixed(0)}%.
            </p>`;
    }

    if (
        before.batteryHealth &&
        after.batteryHealth
    ) {

        const batteryDiff =
            before.batteryHealth
            -
            after.batteryHealth;

        if (batteryDiff > 0) {

            html +=

                `<p>
                Battery Health menurun
                ${batteryDiff.toFixed(0)}%.
                </p>`;
        }
    }

    if (html === "") {

        html =
            "<p>Tidak terdapat perubahan signifikan.</p>";
    }

    summary.innerHTML =
        html;
}

// =========================================
// RECOMMENDATION
// =========================================

function renderRecommendation(
        before,
        after
) {

    const list =
        document.getElementById(
            "recommendationList"
        );

    list.innerHTML = "";

    if (!before || !after) {

        list.innerHTML =
            "<li>Data belum lengkap.</li>";

        return;
    }

    if (
        before.batteryHealth &&
        after.batteryHealth
    ) {

        const batteryDiff =
            before.batteryHealth
            -
            after.batteryHealth;

        if (batteryDiff > 10) {

            list.innerHTML +=
                `
                <li>
                    Lakukan pemeriksaan kesehatan baterai
                    dan pertimbangkan penggantian baterai.
                </li>
                `;
        }
    }

    if (
        after.availableStorage
        <
        after.totalStorage * 0.2
    ) {

        list.innerHTML +=
            `
            <li>
                Ruang penyimpanan tersisa kurang dari 20%.
                Lakukan pembersihan file sementara dan
                arsipkan data yang tidak digunakan.
            </li>
            `;
    }

    if (
        list.innerHTML.trim()
        === ""
    ) {

        list.innerHTML =
            `
            <li>
                Kondisi laptop masih dalam batas normal.
                Lanjutkan pemeliharaan berkala.
            </li>
            `;
    }
}

// =========================================
// EXPORT
// =========================================

function initExportButton(
        cycleId
) {

    const btn =
        document.getElementById(
            "btnExport"
    )
    .addEventListener(
        "click",
        () => {

            const params =
                new URLSearchParams(
                    window.location.search
                );

            const cycleId =
                params.get(
                    "cycleId"
                );

            if (!cycleId) {

                alert(
                    "Cycle ID tidak ditemukan."
                );

                return;
            }

            window.open(
                `/api/reports/compare/${cycleId}`,
                "_blank"
            );
        }
    );
}

// =========================================
// HELPERS
// =========================================

function addRow(
        tbody,
        component,
        beforeValue,
        afterValue
) {

    const row =
        document.createElement("tr");

    let statusText =
        "✔ Same";

    let statusClass =
        "status-same";

    if (
        String(beforeValue).trim()
        !==
        String(afterValue).trim()
    ) {

        statusText =
            "⚠ Changed";

        statusClass =
            "status-warning";
    }

    row.innerHTML = `
        <td>${safe(component)}</td>
        <td>${safe(beforeValue)}</td>
        <td>${safe(afterValue)}</td>
        <td class="${statusClass}">
            ${statusText}
        </td>
    `;

    tbody.appendChild(row);
}

function formatGB(bytes) {

    if (!bytes) {
        return "N/A";
    }

    return (
        bytes
        / 1024
        / 1024
        / 1024
    ).toFixed(2) + " GB";
}

function formatBattery(value) {

    if (
        value === null
        || value === undefined
    ) {

        return "N/A";
    }

    return `${value}%`;
}

function formatDateTime(value) {

    if (!value) {
        return "-";
    }

    return new Date(value)
        .toLocaleString();
}

function safe(value) {

    if (
        value === null
        || value === undefined
        || value === ""
    ) {

        return "-";
    }

    return value;
}

loadCompare();