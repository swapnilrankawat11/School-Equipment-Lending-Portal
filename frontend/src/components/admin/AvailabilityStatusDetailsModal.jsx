import { CircularProgress, Box } from "@mui/material";
import "../../styles/common/DetailsModal.css";

const AvailabilityStatusDetailsModal = ({ availabilityStatus, onClose }) => {
  return (
    <div className="modal-overlay">
      <div className="modal">
        {availabilityStatus ? (
          <>
            <h3>Availability Status Details</h3>
            <ul>
              <li>
                <strong>ID: </strong>
                {availabilityStatus.id}
              </li>
              <li>
                <strong>Status Name: </strong>
                {availabilityStatus.availabilityStatus}
              </li>
              <li>
                <strong>Description: </strong>
                {availabilityStatus.description || "N/A"}
              </li>
              <li>
                <strong>Created At: </strong>
                {new Date(availabilityStatus.createdAt).toLocaleString()}
              </li>
              {availabilityStatus.modifiedAt && (
                <li>
                  <strong>Modified At: </strong>
                  {new Date(availabilityStatus.modifiedAt).toLocaleString()}
                </li>
              )}
            </ul>
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

export default AvailabilityStatusDetailsModal;
