import { useState, useEffect } from "react";
import { updateRequest } from "../../services/admin/requestsApi";
import { getRequestStatusTypes } from "../../services/admin/requestStatusTypesApi";
import { toast } from "react-toastify";
import { CircularProgress } from "@mui/material";
import "../../styles/common/AddEditModal.css";
import "../../styles/admin/ApproveRequestModal.css";
import {useSession} from "../../contexts/SessionContext";

const ApproveRequestModal = ({ selectedRequest, onSuccess, onCancel }) => {
  const {sessionInfo} = useSession();
  const [formData, setFormData] = useState({
    expected_return_date: "",
  });

  const [errors, setErrors] = useState({});
  const [requestStatusTypes, setRequestStatusTypes] = useState([]);
  const [alertMessage, setAlertMessage] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleChange = (e) => {
    setFormData({ [e.target.name]: e.target.value });
    if (errors[e.target.name]) {
      setErrors((prev) => ({ ...prev, [e.target.name]: "" }));
    }
  };

  const fetchStatusTypes = async () => {
    try {
      const response = await getRequestStatusTypes();
      setRequestStatusTypes(response.data);
    } catch (err) {
      const errorMsg =
        err?.response?.data?.error || "Something went wrong! Try again.";
      toast.error(errorMsg);
    }
  };

  useEffect(() => {
    fetchStatusTypes();
  }, []);

  const validateForm = () => {
    const errs = {};

    if (!formData.expected_return_date) {
      errs.expected_return_date =
        "Expected return date is required when approving request.";
    }

    return errs;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setAlertMessage("");
    const validationErrors = validateForm();
    setErrors(validationErrors);

    if (Object.keys(validationErrors).length > 0) return;

    const payload = {
      requestStatusId: Number(
        requestStatusTypes.find(
          (st) => st.status_type.toLowerCase() === "approved"
        ).id
      ),
      expectedReturnDate: formData.expected_return_date || null,
      modifiedBy: sessionInfo?.userId || undefined,
    };

    Object.keys(payload).forEach(
      (key) => payload[key] === undefined && delete payload[key]
    );

    try {
      setIsSubmitting(true);
      await updateRequest(selectedRequest.id, payload);
      setAlertMessage("Request approved successfully!");
      setFormData({
         expected_return_date: "",
      });
      onSuccess();
    } catch (err) {
      const errorMsg =
        err?.response?.data?.error || "Failed to approve the request.";
      toast.error(errorMsg);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <form onSubmit={handleSubmit} className="equipment-form">
          <h3>Approve Request</h3>

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

          <div className="form-field">
            <label>Expected Return Date</label>
            <input
              type="date"
              name="expected_return_date"
              value={formData.expected_return_date}
              onChange={handleChange}
              className={errors.expected_return_date ? "input-error" : ""}
            />
          </div>

          {/* --- Buttons --- */}
          <div className="equipment-form-buttons">
            <button
              className="save-button"
              type="submit"
              disabled={isSubmitting}
            >
              {isSubmitting ? (
                <CircularProgress size={10} style={{ color: "white" }} />
              ) : (
                "Approve"
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

export default ApproveRequestModal;
