import { CircularProgress, Box } from "@mui/material";
import { toTitleCase } from "../../utils/TextFormatter";
import "../../styles/common/DetailsModal.css";

const EquipmentDetailsModal = ({ equipment, onClose }) => {
  return (
    <div className="modal-overlay">
      <div className="modal">
        {equipment ? (
          <>
            <h3>Equipment Details</h3>
            <ul>
              <li>
                <strong>ID: </strong>
                {equipment.id}
              </li>
              <li>
                <strong>Name: </strong>
                {toTitleCase(equipment.name)}
              </li>
              <li>
                <strong>Category: </strong>
                {toTitleCase(equipment.categoryName)}
              </li>
              <li>
                <strong>Availability Status: </strong>
                {toTitleCase(equipment.availabilityStatus)}
              </li>
              <li>
                <strong>Total Quantity: </strong>
                {equipment.totalQuantity}
              </li>
              <li>
                <strong>Available Quantity: </strong>
                {equipment.quantityLeft}
              </li>
              <li>
                <strong>Description: </strong>
                {equipment.description ? equipment.description : "N/A"}
              </li>
              <li>
                <strong>Created At: </strong>
                {new Date(equipment.created_at).toLocaleString()}
              </li>
              {equipment.modified_at && (
                <li>
                  <strong>Modified At: </strong>
                  {new Date(equipment.modified_at).toLocaleString()}
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

export default EquipmentDetailsModal;
