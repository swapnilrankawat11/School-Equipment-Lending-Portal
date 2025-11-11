import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import { toast } from "react-toastify";
import { login } from "../../services/auth/loginApi";
import Button from "@mui/material/Button";
import CircularProgress from "@mui/material/CircularProgress";
import "../../styles/auth/LoginPage.css";
import { useSession } from "../../contexts/SessionContext";

const LoginPage = () => {
  const [formData, setFormData] = useState({ email: "", password: "" });
  const [errors, setErrors] = useState({});
  const [isLoading, setIsLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const navigate = useNavigate();
  const { fetchSessionInfo } = useSession();

  localStorage.removeItem("sessionInfo");

  const validateFormData = () => {
    const errs = {};
    if (!formData.email.trim()) {
      errs.email = "Email is required.";
    } else if (!/^\S+@\S+\.\S+$/.test(formData.email)) {
      errs.email = "Enter a valid email address.";
    }
    if (!formData.password.trim()) {
      errs.password = "Password is required.";
    }
    return errs;
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    setErrors({ ...errors, [e.target.name]: "" });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const validationErrors = validateFormData();
    if (Object.keys(validationErrors).length) {
      setErrors(validationErrors);
      return;
    }

    setIsLoading(true);

    try {
      localStorage.removeItem("sessionInfo");

      const res = await login(formData);

      if (res.data?.success) {
        await fetchSessionInfo();

        const userType = res.data.user.userTypeName?.toLowerCase();
        if (userType === "admin") {
          navigate("/admin", { replace: true });
        } else if (userType === "student") {
          navigate("/student/dashboard", { replace: true });
        } else if (userType === "staff") {
          navigate("/staff", { replace: true });
        }
      } else {
        setErrors({ ...errors, loginError: res.data?.error });
      }
    } catch (err) {
      const msg =
        err.response?.data?.error || "Something went wrong! Please try again.";
      setErrors({ ...errors, loginError: msg });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-wrapper">
        <div className="app-logo">School Equipment Lending Portal</div>
        <div className="login-container">
          <form className="login-form" onSubmit={handleSubmit}>
            {errors.loginError && (
              <div className="login-error-container">
                {errors.loginError}
                <button
                  className="close-errors-btn"
                  type="button"
                  onClick={() => setErrors({ ...errors, loginError: "" })}
                >
                  ×
                </button>
              </div>
            )}

            <label>Email</label>
            <input
              name="email"
              type="text"
              placeholder="Enter email address"
              value={formData.email}
              onChange={handleChange}
              className={errors.email ? "error-input" : ""}
              autoFocus
            />
            {errors.email && <p className="error-text">{errors.email}</p>}

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
              className="login-btn"
              disabled={isLoading}
              sx={{
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                gap: 1,
                color: "white",
              }}
            >
              {isLoading ? (
                <>
                  <CircularProgress size={20} sx={{ color: "white" }} />
                </>
              ) : (
                "Log In"
              )}
            </Button>

            <div className="login-footer">
              Don’t have an account? <Link to="/signup">Create Account</Link>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
