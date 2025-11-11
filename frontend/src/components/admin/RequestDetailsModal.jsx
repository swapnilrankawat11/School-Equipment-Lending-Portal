import { CircularProgress, Box } from "@mui/material";
import { toTitleCase } from "../../utils/TextFormatter";
import "../../styles/common/DetailsModal.css";
import "../../styles/admin/RequestDetailsModal.css";

const RequestDetailsModal = ({ request, onClose }) => {
  return (
    <div className="modal-overlay">
      <div className="modal request-details-modal">
        {request ? (
          <>
            <h3>Request Details</h3>

            <div className="details-section">
              <h4>Equipment Info</h4>
              <div className="details-grid">
                <div>
                  <strong>Name:</strong> {toTitleCase(request.equipmentName)}
                </div>
                <div>
                  <strong>Quantity:</strong> {request.quantityRequested}
                </div>
                <div>
                  <strong>Status:</strong>{" "}
                  {toTitleCase(request.requestStatusName)}
                </div>
              </div>
            </div>

            <div className="details-section">
              <h4>Request Info</h4>
              <div className="details-grid">
                <div>
                  <strong>Purpose:</strong> {request.purpose || "N/A"}
                </div>
                <div>
                  <strong>Expected Return:</strong>{" "}
                  {request.expectedReturnDate || "N/A"}
                </div>

                <div>
                  <strong>Actual Return:</strong>
                  {request.actualReturnDate ? request.actualReturnDate : "N/A"}
                </div>
              </div>
            </div>

            <div className="details-section">
              <h4>Audit Trail</h4>
              <div className="details-grid">
                <div>
                  <strong>Requested By:</strong> {request.createdByName}
                </div>
                <div>
                  <strong>Requested At:</strong>{" "}
                  {new Date(request.createdAt).toLocaleString()}
                </div>

                <div>
                  <strong>Last Modified By:</strong>{" "}
                  {request.modifiedByName.trim()
                    ? request.modifiedByName
                    : "N/A"}
                </div>
                <div>
                  <strong>Last Modified At:</strong>{" "}
                  {new Date(request.modifiedAt).toLocaleString()}
                </div>
              </div>
            </div>
          </>
        ) : (
          <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
            height="300px"
          >
            <CircularProgress />
          </Box>
        )}

        <button type="button" onClick={onClose}>
          Close
        </button>
      </div>
    </div>
  );
};

export default RequestDetailsModal;
