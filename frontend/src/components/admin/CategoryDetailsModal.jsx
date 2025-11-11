import { CircularProgress, Box } from "@mui/material";
import { toTitleCase } from "../../utils/TextFormatter";
import "../../styles/common/DetailsModal.css";

const CategoryDetailsModal = ({ category, onClose }) => {
  return (
    <div className="modal-overlay">
      <div className="modal">
        {category ? (
          <>
            <h3>Category Details</h3>
            <ul>
              <li>
                <strong>ID: </strong>
                {category.id}
              </li>
              <li>
                <strong>Category Name: </strong>
                {toTitleCase(category.categoryName)}
              </li>
              <li>
                <strong>Description: </strong>
                {toTitleCase(category.description || "N/A")}
              </li>
              <li>
                <strong>Created At: </strong>
                {new Date(category.createdAt).toLocaleString()}
              </li>
              {category.modifiedAt && (
                <li>
                  <strong>Modified At: </strong>
                  {new Date(category.modifiedAt).toLocaleString()}
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

export default CategoryDetailsModal;
