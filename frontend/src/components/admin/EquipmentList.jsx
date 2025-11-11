import { useState } from "react";
import { DataGrid } from "@mui/x-data-grid";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import CircularProgress from "@mui/material/CircularProgress";
import DialogTitle from "@mui/material/DialogTitle";
import "../../styles/common/TableActionButtons.css";

const EquipmentList = ({
  equipments,
  loading,
  onEdit,
  onViewDetails,
  onDelete,
  role,
}) => {
  const [showConfirmDeleteDialog, setShowConfirmDeleteDialog] = useState(false);
  const [selectedEquipmentId, setSelectedEquipmentId] = useState(null);
  const [isDeleting, setIsDeleting] = useState(false);

  // Map rows for DataGrid
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
      field: "sno",
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
            {role !== "staff" && (
              <button
                className="edit-button"
                onClick={() => onEdit(originalEquipment)}
              >
                Edit
              </button>
            )}

            <button
              className="view-details-button"
              onClick={() => onViewDetails(originalEquipment)}
            >
              Details
            </button>
            {role !== "staff" && (
              <button
                className="delete-button"
                onClick={() => {
                  setSelectedEquipmentId(originalEquipment.id);
                  setShowConfirmDeleteDialog(true);
                }}
              >
                Delete
              </button>
            )}
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

      {/* Delete Confirmation Dialog */}
      <Dialog
        open={showConfirmDeleteDialog}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">{"Delete Equipment"}</DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            This action will permanently delete the selected equipment.
            <br />
            Are you sure you want to proceed?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={async () => {
              setIsDeleting(true);
              try {
                await onDelete(selectedEquipmentId);
              } catch (error) {
                console.error("Delete failed:", error);
              } finally {
                setShowConfirmDeleteDialog(false);
                setIsDeleting(false);
              }
            }}
            autoFocus
            color="error"
            disabled={isDeleting}
            sx={{
              "&:hover": {
                cursor: isDeleting ? "not-allowed" : "pointer",
              },
            }}
          >
            {isDeleting ? (
              <CircularProgress size={18} style={{ color: "red" }} />
            ) : (
              "Yes"
            )}
          </Button>

          <Button onClick={() => setShowConfirmDeleteDialog(false)}>No</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
};

export default EquipmentList;
