let allData = [];

async function loadHistory() {

    const response =
        await fetch(
            "/api/device-checks/history-summary"
        );

    allData =
        await response.json();

    renderTable(allData);
}

function renderTable(data) {

    const tbody =
        document.getElementById(
            "historyBody"
        );

    tbody.innerHTML = "";

    data.forEach(item => {

        const row =
            document.createElement("tr");

        const statusClass =

            item.status === "COMPLETED"

                ? "badge-success"

                : "badge-warning";

        row.innerHTML = `
            <td>
                ${item.serialNumber || "-"}
            </td>

            <td>
                ${item.borrowerName || "-"}
            </td>

            <td>
                ${formatTime(item.beforeTime)}
            </td>

            <td>
                ${formatTime(item.afterTime)}
            </td>

            <td class="${statusClass}">
                ${item.status}
            </td>

            <td>

                <button
                    class="btn-compare"

                    onclick="openCompare(
                        '${item.inspectionCycleId}'
                    )">

                    Compare

                </button>

            </td>
        `;

        tbody.appendChild(row);
    });
}

function openCompare(cycleId) {

    window.location.href =

        `compare.html?cycleId=${cycleId}`;
}

function formatTime(value) {

    if (!value || value === "-") {

        return "-";
    }

    const date =
        new Date(value);

    return date.toLocaleString();
}

/* =========================================
   SEARCH
========================================= */

document
    .getElementById("searchInput")

    .addEventListener(

        "input",

        function () {

            const keyword =

                this.value
                    .toLowerCase();

            const filtered =

                allData.filter(item =>

                    item.serialNumber
                        ?.toLowerCase()
                        .includes(keyword)
                );

            renderTable(filtered);
        }
    );

loadHistory();