import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import { signup } from "../../services/auth/singupApi";
import { getUserTypes } from "../../services/auth/userTypesApi";
import { toast } from "react-toastify";
import Button from "@mui/material/Button";
import { toTitleCase } from "../../utils/TextFormatter";
import CircularProgress from "@mui/material/CircularProgress";
import "../../styles/auth/SingupPage.css";

const SignupPage = () => {
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    userTypeId: "",
  });

  const [userTypes, setUserTypes] = useState([]);
  const [errors, setErrors] = useState({});
  const [apiError, setApiError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");

  useEffect(() => {
    const fetchUserTypes = async () => {
      try {
        const res = await getUserTypes();
        setUserTypes(res.data);
      } catch (err) {
        toast.error("Failed to load user types.");
      }
    };
    fetchUserTypes();
  }, []);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
    setErrors({ ...errors, [e.target.name]: "" });
    setApiError("");
  };

  const validateFormData = () => {
    const errs = {};
    const nameRegex = /^[A-Za-z\s]+$/;

    if (!formData.firstName.trim()) {
      errs.firstName = "First Name is required.";
    } else if (!nameRegex.test(formData.firstName)) {
      errs.firstName = "First Name can only contain letters and spaces.";
    }

    if (!formData.lastName.trim()) {
      errs.lastName = "Last Name is required.";
    } else if (!nameRegex.test(formData.lastName)) {
      errs.lastName = "Last Name can only contain letters and spaces.";
    }

    if (!formData.email.trim()) {
      errs.email = "Email address is required.";
    } else if (!/^\S+@\S+\.\S+$/.test(formData.email)) {
      errs.email = "Enter a valid email address.";
    }

    if (!formData.userTypeId) {
      errs.userTypeId = "Please select a user type.";
    }

    if (!formData.password) {
      errs.password = "Password is required.";
    } else if (formData.password.length < 6) {
      errs.password = "Password must be at least 6 characters.";
    }

    return errs;
  };

  const handleSignup = async () => {
    setIsLoading(true);
    try {
      const payload = {
        firstName: formData.firstName,
        lastName: formData.lastName,
        email: formData.email,
        password: formData.password,
        userTypeId: Number(formData.userTypeId),
      };

      const response = await signup(payload);
      if (response?.data?.success) {
        setSuccessMessage("Account created successfully!");
        setFormData({
          firstName: "",
          lastName: "",
          email: "",
          password: "",
          userTypeId: "",
        });
        setErrors({});
        setApiError("");
      } else {
        setApiError("Something went wrong! Try Again.");
      }
    } catch (err) {
      const errorMsg =
        err?.response?.data?.error || "Something went wrong! Try Again.";
      setApiError(errorMsg);
    } finally {
      setIsLoading(false);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const validationErrors = validateFormData();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      setApiError("");
    } else {
      handleSignup();
    }
  };

  return (
    <div className="signup-page">
      <div className="signup-wrapper">
        <div className="app-logo">School Equipment Lending Portal</div>

        <div className="signup-container">
          <form className="signup-form" onSubmit={handleSubmit}>
            {apiError && (
              <div className="login-error-container">
                {apiError}
                <button
                  className="close-errors-btn"
                  type="button"
                  onClick={() => setApiError("")}
                >
                  ×
                </button>
              </div>
            )}

            {successMessage && (
              <div className="form-success-submit">
                {successMessage}
                <button
                  className="close-message-btn"
                  type="button"
                  onClick={() => setSuccessMessage("")}
                >
                  ×
                </button>
              </div>
            )}

            <label>First Name</label>
            <input
              autoFocus
              type="text"
              name="firstName"
              placeholder="Enter your first name"
              value={formData.firstName}
              onChange={handleChange}
              className={errors.firstName ? "error-input" : ""}
            />
            {errors.firstName && (
              <p className="error-text">{errors.firstName}</p>
            )}

            <label>Last Name</label>
            <input
              type="text"
              name="lastName"
              placeholder="Enter your last name"
              value={formData.lastName}
              onChange={handleChange}
              className={errors.lastName ? "error-input" : ""}
            />
            {errors.lastName && <p className="error-text">{errors.lastName}</p>}

            <label>Email</label>
            <input
              type="text"
              name="email"
              placeholder="Enter email address"
              value={formData.email}
              onChange={handleChange}
              className={errors.email ? "error-input" : ""}
            />
            {errors.email && <p className="error-text">{errors.email}</p>}

            <label>User Type</label>
            <select
              name="userTypeId"
              value={formData.userTypeId}
              onChange={handleChange}
              className={`user-type-select ${
                errors.userTypeId ? "error-input" : ""
              }`}
            >
              <option value="">Select User Type</option>
              {userTypes.map((type) => (
                <option key={type.id} value={type.id}>
                  {toTitleCase(type.userType)}
                </option>
              ))}
            </select>
            {errors.userTypeId && (
              <p className="error-text">{errors.userTypeId}</p>
            )}

            <label>Password</label>
            <div className="password-input-container">
              <input
                name="password"
                type={showPassword ? "text" : "password"}
                placeholder="Enter password"
                value={formData.password}
                onChange={handleChange}
                className={errors.password ? "error-input" : ""}
              />
              <button
                type="button"
                className="toggle-password-btn"
                onClick={() => setShowPassword(!showPassword)}
              >
                {showPassword ? <FaEyeSlash /> : <FaEye />}
              </button>
            </div>
            {errors.password && <p className="error-text">{errors.password}</p>}

            <Button
              type="submit"
              variant="contained"
              className="signup-btn"
              disabled={isLoading}
              sx={{
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                gap: 1,
              }}
            >
              {isLoading ? (
                <>
                  <CircularProgress size={20} sx={{ color: "white" }} />
                </>
              ) : (
                "Sign Up"
              )}
            </Button>

            <div className="signup-footer">
              Already have an account? <Link to="/login">Back to Login</Link>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default SignupPage;
