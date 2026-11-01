import { useState, useEffect } from "react";
import {
  addCategory,
  updateCategory,
} from "../../services/admin/categoriesApi";
import { toast } from "react-toastify";
import { toTitleCase } from "../../utils/TextFormatter";
import CircularProgress from "@mui/material/CircularProgress";
import "../../styles/common/AddEditModal.css";
import "../../styles/admin/AddEditCategoryModal.css";
import { useSession } from "../../contexts/SessionContext";

const AddEditCategoryModal = ({
  editingCategoryDetails,
  onSuccess,
  onCancel,
}) => {
  const [formData, setFormData] = useState({
    categoryName: "",
    description: "",
  });

  const {sessionInfo} = useSession();
  const [errors, setErrors] = useState({});
  const [alertMessage, setAlertMessage] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Pre-fill form if editing
  useEffect(() => {
    if (editingCategoryDetails) {
      setFormData({
        categoryName: editingCategoryDetails.categoryName,
        description: editingCategoryDetails.description,
      });
    } else {
      setFormData({
        categoryName: "",
        description: "",
      });
    }
  }, [editingCategoryDetails]);

  // Handle input changes
  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    if (e.target.value) e.target.className = "";
  };

  // Validate form fields
  const validateFormData = () => {
    const errs = {};
    const nameRegex = /^[A-Za-z0-9\s-]+$/;

    if (!formData.categoryName || !formData.categoryName.trim()) {
      errs.categoryName = "Category name is required.";
    } else if (formData.categoryName.length > 100) {
      errs.categoryName = "Category name must be less than 100 characters.";
    } else if (!nameRegex.test(formData.categoryName.trim())) {
      errs.categoryName =
        "Category name can only contain letters, numbers, spaces, and hyphens.";
    }

    if (!formData.description || !formData.description.trim()) {
      errs.description = "Description is required.";
    } else if (formData.description.length > 255) {
      errs.description = "Description must be less than 255 characters.";
    }

    return errs;
  };

  // Handle submit
  const handleSubmit = async (e) => {
    e.preventDefault();
    setAlertMessage("");
    const validationErrors = validateFormData();
    setErrors(validationErrors);

    if (Object.keys(validationErrors).length > 0) return;

    // Prepare payload including created_by or modified_by from session userId
    const payload = {
      categoryName: toTitleCase(formData.categoryName.trim()),
      description: formData.description,
      createdBy: !editingCategoryDetails ? sessionInfo?.userId : undefined,
      modifiedBy: editingCategoryDetails ? sessionInfo?.userId : undefined,
    };

    // Remove undefined keys
    Object.keys(payload).forEach(
      (key) => payload[key] === undefined && delete payload[key]
    );

    try {
      setIsSubmitting(true);
      if (editingCategoryDetails) {
        await updateCategory(editingCategoryDetails.id, payload);
        setAlertMessage("Updated Successfully.");
      } else {
        await addCategory(payload);
        setAlertMessage("Added Successfully.");
        setFormData({
          categoryName: "",
          description: "",
        });
      }
      onSuccess();
    } catch (err) {
      const errorMsg =
        err?.response?.data?.error || "Something went wrong! Try Again.";
      toast.error(errorMsg);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <form onSubmit={handleSubmit} className="category-form">
          <h3>{editingCategoryDetails ? "Update" : "Add"} Category</h3>

          {/* Validation Errors */}
          {Object.keys(errors).length > 0 && (
            <div className="form-errors">
              <button
                className="close-errors-btn"
                type="button"
                onClick={() => setErrors({})}
              >
                ×
              </button>
              {Object.values(errors).map((err, i) => (
                <p key={i}>{err}</p>
              ))}
            </div>
          )}

          {/* Success Alert */}
          {alertMessage && (
            <div className="form-success-submit">
              {alertMessage}
              <button
                className="close-alert-btn"
                type="button"
                onClick={() => setAlertMessage("")}
              >
                ×
              </button>
            </div>
          )}

          {/* Category Name Input */}
          <input
            autoFocus
            type="text"
            name="categoryName"
            placeholder="Enter category name"
            value={formData.categoryName}
            onChange={handleChange}
            className={errors.categoryName ? "input-error" : ""}
          />

          {/* Description Input */}
          <input
            type="text"
            name="description"
            placeholder="Enter description"
            value={formData.description}
            onChange={handleChange}
            className={errors.description ? "input-error" : ""}
          />

          {/* Buttons */}
          <div className="category-form-buttons">
            <button
              className="save-button"
              type="submit"
              disabled={isSubmitting}
            >
              {isSubmitting ? (
                <CircularProgress size={10} style={{ color: "white" }} />
              ) : editingCategoryDetails ? (
                "Save"
              ) : (
                "Add"
              )}
            </button>

            <button className="close-button" type="button" onClick={onCancel}>
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddEditCategoryModal;
