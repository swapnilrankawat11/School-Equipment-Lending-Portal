import { useState } from "react";
import { createEquipmentRequest } from "../../services/student/equipmentRequestApi";
import { toast } from "react-toastify";
import CircularProgress from "@mui/material/CircularProgress";
import "../../styles/student/EquipmentRequestModal.css";
import "../../styles/common/AddEditModal.css";
import { useSession } from "../../contexts/SessionContext";

const EquipmentRequestModal = ({ requestedEquipment, onCancel }) => {
  const [formData, setFormData] = useState({
    name: requestedEquipment.name,
    quantityRequested: "",
    purpose: "",
  });

  const {sessionInfo} = useSession();
  const [errors, setErrors] = useState({});
  const [alertMessage, setAlertMessage] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Handle field changes
  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    if (e.target.value) e.target.className = "";
  };

  // Validate input fields
  const validateFormData = () => {
    const errs = {};

    if (!formData.name || !formData.name.trim()) {
      errs.name = "Equipment Name is required.";
    }

    if (!formData.quantityRequested || isNaN(formData.quantityRequested)) {
      errs.quantityRequested = "Quantity must be a valid number.";
    } else if (Number(formData.quantityRequested) <= 0) {
      errs.quantityRequested = "Quantity must be greater than zero.";
    }

    if (!formData.purpose || !formData.purpose.trim()) {
      errs.purpose = "Purpose is required.";
    } else if (formData.purpose.length > 255) {
      errs.purpose = "Purpose must be less than 255 characters.";
    }

    return errs;
  };

  // Handle form submit
  const handleSubmit = async (e) => {
    e.preventDefault();
    setAlertMessage("");
    const validationErrors = validateFormData();
    setErrors(validationErrors);

    if (Object.keys(validationErrors).length > 0) return;

    const payload = {
      equipmentId: requestedEquipment.id,
      quantityRequested: Number(formData.quantityRequested),
      purpose: formData.purpose,
      createdBy: sessionInfo?.userId, // Added createdBy from session
    };

    try {
      setIsSubmitting(true);
      await createEquipmentRequest(payload);
      setAlertMessage("Requested Successfully.");
      setFormData({
        quantityRequested: "",
        purpose: "",
      });
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
        <form onSubmit={handleSubmit} className="equipment-request-form">
          <h3>Request Equipment</h3>

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

          {/* Input Fields */}
          <input
            disabled
            type="text"
            name="name"
            value={formData.name}
            className={errors.name ? "input-error" : ""}
          />

          <input
            type="number"
            name="quantityRequested"
            placeholder="Enter quantity"
            value={formData.quantityRequested}
            onChange={handleChange}
            className={errors.quantityRequested ? "input-error" : ""}
          />

          <input
            type="text"
            name="purpose"
            placeholder="Enter purpose/remark"
            value={formData.purpose}
            onChange={handleChange}
            className={errors.purpose ? "input-error" : ""}
          />

          {/* Buttons */}
          <div className="equipment-request-form-buttons">
            <button
              className="save-button"
              type="submit"
              disabled={isSubmitting}
            >
              {isSubmitting ? (
                <CircularProgress size={10} style={{ color: "white" }} />
              ) : (
                "Request"
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

export default EquipmentRequestModal;
