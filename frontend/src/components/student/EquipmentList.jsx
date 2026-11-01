import { useState } from "react";
import { DataGrid } from "@mui/x-data-grid";
import "../../styles/common/TableActionButtons.css";

const EquipmentList = ({ equipments, loading, onRequest, onViewDetails }) => {
  const rows = equipments.map((row, index) => ({
    ...row,
    sno: index + 1,
  }));

  const [paginationModel, setPaginationModel] = useState({
    page: 0,
    pageSize: 10,
  });

  // Define columns
  const columns = [
    {
      field: "id",
      headerName: "S. No",
      headerAlign: "center",
      align: "center",
      width: 80,
      sortable: false,
    },
    {
      field: "name",
      headerName: "Equipment Name",
      headerAlign: "center",
      align: "center",
      flex: 1.2,
    },
    {
      field: "categoryName",
      headerName: "Category",
      headerAlign: "center",
      align: "center",
      flex: 1,
    },
    {
      field: "availabilityStatus",
      headerName: "Availability",
      headerAlign: "center",
      align: "center",
      flex: 1,
    },
    {
      field: "totalQuantity",
      headerName: "Total Quantity",
      headerAlign: "center",
      align: "center",
      flex: 1,
    },
    {
      field: "actions",
      headerName: "Operations",
      headerAlign: "center",
      align: "center",
      flex: 1.2,
      sortable: false,
      filterable: false,
      disableColumnMenu: true,
      renderCell: (params) => {
        const originalEquipment = equipments.find(
          (eq) => eq.id === params.row.id
        );
        return (
          <div>
            <button
              disabled={originalEquipment.availabilityId !== 1}
              className="edit-button"
              onClick={() => onRequest(originalEquipment)}
            >
              Request
            </button>
            <button
              className="view-details-button"
              onClick={() => onViewDetails(originalEquipment)}
            >
              Details
            </button>
          </div>
        );
      },
    },
  ];

  return (
    <div>
      <div
        style={{
          height: rows.length >= 7 ? 500 : "auto",
          width: "100%",
          backgroundColor: "white",
          borderRadius: "8px",
        }}
      >
        <DataGrid
          rows={rows}
          columns={columns}
          loading={loading}
          paginationModel={paginationModel}
          onPaginationModelChange={setPaginationModel}
          pageSizeOptions={[10, 15, 25]}
          disableRowSelectionOnClick
          disableColumnSelector
          hideFooterSelectedRowCount
          getRowId={(row) => row.id}
          sx={{
            "& .MuiDataGrid-cell:focus-within, & .MuiDataGrid-cell:focus": {
              outline: "none !important",
            },
            "& .MuiDataGrid-columnHeader": {
              backgroundColor: "#f1f3f5",
            },
            "& .MuiDataGrid-columnHeaderTitle": {
              fontWeight: "bold",
            },
            "& .MuiDataGrid-cell, & .MuiDataGrid-columnHeader": {
              borderRight: "1px solid #e0e0e0",
              borderBottom: "1px solid #e0e0e0",
            },
            "& .MuiDataGrid-iconSeparator": {
              display: "none",
            },
          }}
        />
      </div>
    </div>
  );
};

export default EquipmentList;
