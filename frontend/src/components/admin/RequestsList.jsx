import { useState } from "react";
import { DataGrid } from "@mui/x-data-grid";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import Button from "@mui/material/Button";
import CircularProgress from "@mui/material/CircularProgress";
import "../../styles/common/TableActionButtons.css";

const RequestList = ({
  requests,
  loading,
  onManage,
  onViewDetails,
  statusType,
  onClose,
  onReject,
  onCancel, // for student cancel
  studentView = false, // if true, render cancel button for pending
}) => {
  const [paginationModel, setPaginationModel] = useState({
    page: 0,
    pageSize: 10,
  });

  const [showConfirmDialog, setShowConfirmDialog] = useState(false);
  const [selectedRequest, setSelectedRequest] = useState(null);
  const [actionType, setActionType] = useState("");
  const [isProcessing, setIsProcessing] = useState(false);

  const rows = requests.map((row, index) => ({
    ...row,
    sno: index + 1,
  }));

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
      field: "equipmentName",
      headerName: "Equipment Name",
      headerAlign: "center",
      align: "center",
      flex: 1.5,
    },
    {
      field: "createdByName",
      headerName: "Requested By",
      headerAlign: "center",
      align: "center",
      flex: 1.5,
    },
    {
      field: "createdAt",
      headerName: "Requested At",
      headerAlign: "center",
      align: "center",
      flex: 0.8,
      renderCell: (params) => new Date(params.value).toLocaleDateString(),
    },
    {
      field: "quantityRequested",
      headerName: "Qty Requested",
      headerAlign: "center",
      align: "center",
      flex: 0.8,
    },
    {
      field: "requestStatusName",
      headerName: "Status",
      headerAlign: "center",
      align: "center",
      flex: 0.8,
      renderCell: (params) => {
        const status = params.value?.toLowerCase();
        let color = "gray";
        if (status === "approved") color = "green";
        else if (status === "pending") color = "orange";
        else if (status === "rejected") color = "red";
        else if (status === "closed") color = "blue";

        return (
          <span
            style={{ color, fontWeight: "bold", textTransform: "capitalize" }}
          >
            {params.value}
          </span>
        );
      },
    },
    {
      field: "actions",
      headerName: "Actions",
      headerAlign: "center",
      align: "center",
      flex: 1.8,
      sortable: false,
      filterable: false,
      disableColumnMenu: true,
      renderCell: (params) => {
        const originalRequest = requests.find((r) => r.id === params.row.id);
        const status = originalRequest.requestStatusName?.toLowerCase();

        if (studentView) {
          // STUDENT VIEW
          return (
            <div className="action-buttons">
              {status === "pending" && (
                <>
                  <button
                    className="reject-button"
                    onClick={() => {
                      setSelectedRequest(originalRequest);
                      setActionType("cancel");
                      setShowConfirmDialog(true);
                    }}
                  >
                    Cancel
                  </button>
                  <button
                    className="view-details-button"
                    onClick={() => onViewDetails(originalRequest)}
                  >
                    Details
                  </button>
                </>
              )}
              {status !== "pending" && (
                <button
                  className="view-details-button"
                  onClick={() => onViewDetails(originalRequest)}
                >
                  Details
                </button>
              )}
            </div>
          );
        }

        // Admin view
        return (
          <div className="action-buttons">
            {status === "pending" && (
              <>
                <button
                  className="approve-button"
                  onClick={() => onManage(originalRequest)}
                >
                  Approve
                </button>
                <button
                  className="reject-button"
                  onClick={() => {
                    setSelectedRequest(originalRequest);
                    setActionType("reject");
                    setShowConfirmDialog(true);
                  }}
                >
                  Reject
                </button>
                <button
                  className="view-details-button"
                  onClick={() => onViewDetails(originalRequest)}
                >
                  Details
                </button>
              </>
            )}

            {status === "approved" && (
              <>
                <button
                  className="close-button"
                  onClick={() => {
                    setSelectedRequest(originalRequest);
                    setActionType("close");
                    setShowConfirmDialog(true);
                  }}
                >
                  Close
                </button>
                <button
                  className="view-details-button"
                  onClick={() => onViewDetails(originalRequest)}
                >
                  Details
                </button>
              </>
            )}

            {(status === "rejected" || status === "closed") && (
              <button
                className="view-details-button"
                onClick={() => onViewDetails(originalRequest)}
              >
                Details
              </button>
            )}
          </div>
        );
      },
    },
  ];

  const handleConfirmAction = async () => {
    if (!selectedRequest || !actionType) return;
    setIsProcessing(true);

    try {
      if (actionType === "close") await onClose(selectedRequest);
      else if (actionType === "reject") await onReject(selectedRequest);
      else if (actionType === "cancel") await onCancel(selectedRequest);
    } catch (err) {
      console.error(err);
    } finally {
      setIsProcessing(false);
      setShowConfirmDialog(false);
      setTimeout(() => {
        setSelectedRequest(null);
        setActionType("");
      }, 300);
    }
  };

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
          "& .MuiDataGrid-columnHeader": { backgroundColor: "#f1f3f5" },
          "& .MuiDataGrid-columnHeaderTitle": { fontWeight: "bold" },
          "& .MuiDataGrid-cell, & .MuiDataGrid-columnHeader": {
            borderRight: "1px solid #e0e0e0",
            borderBottom: "1px solid #e0e0e0",
          },
          "& .MuiDataGrid-iconSeparator": { display: "none" },
        }}
      />

      {/* Confirmation Dialog */}
      <Dialog
        open={showConfirmDialog}
        aria-labelledby="confirm-dialog-title"
        aria-describedby="confirm-dialog-description"
      >
        <DialogTitle id="confirm-dialog-title">
          {actionType === "close"
            ? "Close Request"
            : actionType === "reject"
            ? "Reject Request"
            : "Cancel Request"}
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="confirm-dialog-description">
            {actionType === "close" &&
              "This action will permanently mark the request as closed. Are you sure you want to proceed?"}
            {actionType === "reject" &&
              "This action will permanently reject the request. Are you sure you want to proceed?"}
            {actionType === "cancel" &&
              "This action will permanently cancel your request. Are you sure you want to proceed?"}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={handleConfirmAction}
            color={actionType === "reject" ? "error" : "primary"}
            autoFocus
            disabled={isProcessing}
            sx={{
              "&:hover": { cursor: isProcessing ? "not-allowed" : "pointer" },
            }}
          >
            {isProcessing ? (
              <CircularProgress size={18} color="inherit" />
            ) : (
              "Yes"
            )}
          </Button>
          <Button
            onClick={() => {
              setShowConfirmDialog(false);
              setTimeout(() => {
                setSelectedRequest(null);
                setActionType("");
              }, 300);
            }}
          >
            No
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
};

export default RequestList;
