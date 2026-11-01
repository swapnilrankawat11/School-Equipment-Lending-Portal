import { useEffect, useState } from "react";
import {
  addEquipment,
  updateEquipment,
} from "../../services/admin/equipmentsApi";
import { getCategories } from "../../services/admin/categoriesApi";
import { getAvailabilityStatuses } from "../../services/admin/availabilityStatusApi";
import { toast } from "react-toastify";
import { toTitleCase } from "../../utils/TextFormatter";
import CircularProgress from "@mui/material/CircularProgress";
import "../../styles/admin/AddEditEquipmentModal.css";
import "../../styles/common/AddEditModal.css";
import { useSession } from "../../contexts/SessionContext";

const AddEditEquipmentModal = ({
  editingEquipmentDetails,
  onSuccess,
  onCancel,
}) => {
  const [formData, setFormData] = useState({
    name: "",
    quantity: "",
    category_id: "",
    availability_status_id: "",
    description: "",
  });

  const {sessionInfo} = useSession();
  const [categories, setCategories] = useState([]);
  const [availabilityStatuses, setAvailabilityStatuses] = useState([]);
  const [errors, setErrors] = useState({});
  const [alertMessage, setAlertMessage] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Fetch categories and availability statuses
  const fetchFormData = async () => {
    try {
      const [categoriesRes, availabilityStatusesRes] = await Promise.all([
        getCategories(),
        getAvailabilityStatuses(),
      ]);
      setCategories(categoriesRes.data);
      setAvailabilityStatuses(availabilityStatusesRes.data);
    } catch (err) {
      const errorMsg =
        err?.response?.data?.error || "Something went wrong! Try Again.";
      toast.error(errorMsg);
    }
  };

  useEffect(() => {
    fetchFormData();
  }, []);

  // Pre-fill form if editing
  useEffect(() => {
    if (editingEquipmentDetails) {
      setFormData({
        name: editingEquipmentDetails.name,
        quantity: editingEquipmentDetails.totalQuantity,
        category_id: editingEquipmentDetails.categoryId,
        availability_status_id: editingEquipmentDetails.availabilityId,
        description: editingEquipmentDetails.description,
      });
    } else {
      const availabilityStatus = availabilityStatuses.find(
        (st) => st.availabilityStatus.toLowerCase() === "available"
      );
      setFormData((prev) => ({
        ...prev,
        availability_status_id: availabilityStatus ? availabilityStatus.id : "",
      }));
    }
  }, [editingEquipmentDetails, availabilityStatuses]);

  // Handle input field change
  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    if (e.target.value) e.target.className = "";
  };

  // Validate inputs
  const validateFormData = () => {
    const errs = {};
    const nameRegex = /^[A-Za-z0-9\s-]+$/;

    if (!formData.name || !formData.name.trim()) {
      errs.name = "Equipment name is required.";
    } else if (formData.name.length > 100) {
      errs.name = "Equipment name must be less than 100 characters.";
    } else if (!nameRegex.test(formData.name.trim())) {
      errs.name =
        "Equipment name can only contain letters, numbers, spaces, and hyphens.";
    }

    if (!formData.quantity || isNaN(formData.quantity)) {
      errs.quantity = "Quantity must be a valid number.";
    } else if (Number(formData.quantity) <= 0) {
      errs.quantity = "Quantity must be greater than zero.";
    }

    if (!formData.category_id) errs.category_id = "Category is required.";
    if (!formData.availability_status_id)
      errs.availability_status_id = "Availability Status is required.";

    if (formData.description.length > 255) {
      errs.description = "Description must be less than 255 characters.";
    }

    return errs;
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    setAlertMessage("");
    const validationErrors = validateFormData();
    setErrors(validationErrors);

    if (Object.keys(validationErrors).length > 0) return;

    // Prepare payload with created_by or modified_by from session userId
    const payload = {
      name: formData.name.trim(),
      quantity: Number(formData.quantity),
      categoryId: Number(formData.category_id),
      availabilityId: formData.availability_status_id,
      totalQuantity: Number(formData.quantity),
      description: formData.description,
      created_by: !editingEquipmentDetails ? sessionInfo.userId : undefined,
      modified_by: editingEquipmentDetails ? sessionInfo.userId : undefined,
    };

    // Remove undefined keys
    Object.keys(payload).forEach(
      (key) => payload[key] === undefined && delete payload[key]
    );

    try {
      setIsSubmitting(true);
      if (editingEquipmentDetails) {
        await updateEquipment(editingEquipmentDetails.id, payload);
        setAlertMessage("Updated Successfully.");
      } else {
        await addEquipment(payload);
        setAlertMessage("Added Successfully.");
        setFormData({
          name: "",
          quantity: "",
          category_id: "",
          availability_status_id: "",
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
        <form onSubmit={handleSubmit} className="equipment-form">
          <h3>{editingEquipmentDetails ? "Update" : "Add"} Equipment</h3>

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

          {/* Inputs */}
          <input
            autoFocus
            type="text"
            name="name"
            placeholder="Enter equipment name"
            value={formData.name}
            onChange={handleChange}
            className={errors.name ? "input-error" : ""}
          />

          <select
            name="category_id"
            value={formData.category_id}
            onChange={handleChange}
            className={errors.category_id ? "input-error" : ""}
          >
            <option value="">Select Category</option>
            {categories.map((cat) => (
              <option key={cat.id} value={cat.id}>
                {toTitleCase(cat.categoryName)}
              </option>
            ))}
          </select>

          <select
            name="availability_status_id"
            value={formData.availability_status_id}
            onChange={handleChange}
            className={errors.availability_status_id ? "input-error" : ""}
          >
            <option value="">Select Availability Status</option>
            {availabilityStatuses.map((st) => {
              const status = st.availabilityStatus.toLowerCase();
              const isAddMode = !editingEquipmentDetails;

              const shouldShow = isAddMode
                ? status === "available" // Add mode → only available
                : [
                    "available",
                    "lent out",
                    "under maintenance",
                    "unavailable",
                  ].includes(status);

              const shouldDisable =
                !isAddMode && (status === "available" || status === "lent out");

              if (!shouldShow) return null;

              return (
                <option key={st.id} value={st.id} disabled={shouldDisable}>
                  {toTitleCase(st.availabilityStatus)}
                </option>
              );
            })}
          </select>

          <input
            type="number"
            name="quantity"
            placeholder="Enter quantity"
            value={formData.quantity}
            onChange={handleChange}
            className={errors.quantity ? "input-error" : ""}
          />

          <input
            type="text"
            name="description"
            placeholder="Enter description"
            value={formData.description}
            onChange={handleChange}
            className={errors.description ? "input-error" : ""}
          />

          {/* Buttons */}
          <div className="equipment-form-buttons">
            <button
              className="save-button"
              type="submit"
              disabled={isSubmitting}
            >
              {isSubmitting ? (
                <CircularProgress size={10} style={{ color: "white" }} />
              ) : editingEquipmentDetails ? (
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

export default AddEditEquipmentModal;
