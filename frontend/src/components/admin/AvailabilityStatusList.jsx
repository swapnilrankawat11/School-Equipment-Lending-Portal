import { useState } from "react";
import { DataGrid } from "@mui/x-data-grid";

const AvailabilityStatusList = ({ availabilityStatuses, loading, onViewDetails }) => {
  const [paginationModel, setPaginationModel] = useState({
    page: 0,
    pageSize: 10,
  });

  // Map rows for DataGrid
  const rows = availabilityStatuses.map((row, index) => ({
    ...row,
    sno: index + 1,
  }));

  // Define columns
  const columns = [
    {
      field: "sno",
      headerName: "S. No",
      headerAlign: "center",
      align: "center",
      width: 80,
      sortable: false,
    },
    {
      field: "availabilityStatus",
      headerName: "Status Name",
      headerAlign: "center",
      align: "center",
      flex: 1.0,
    },
    {
      field: "description",
      headerName: "Description",
      headerAlign: "center",
      align: "center",
      flex: 1.5,
    },
    {
      field: "actions",
      headerName: "Actions",
      headerAlign: "center",
      align: "center",
      flex: 1,
      sortable: false,
      filterable: false,
      disableColumnMenu: true,
      renderCell: (params) => {
        const originalStatus = availabilityStatuses.find(
          (status) => status.id === params.row.id
        );
        return (
          <button
            className="view-details-button"
            onClick={() => onViewDetails(originalStatus)}
          >
            Details
          </button>
        );
      },
    },
  ];

  return (
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
  );
};

export default AvailabilityStatusList;
