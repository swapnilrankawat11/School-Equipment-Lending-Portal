import { NavLink, useNavigate } from "react-router-dom";
import { useState } from "react";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import CircularProgress from "@mui/material/CircularProgress";
import { useSession } from "../../contexts/SessionContext";
import { logoutApi } from "../../services/auth/logoutApi";
import "../../styles/common/Sidebar.css";

const Sidebar = () => {
  const navigate = useNavigate();
  const { logout } = useSession();
  const [showConfirmLogoutDialog, setShowConfirmLogoutDialog] = useState(false);
  const [isLoggingOut, setIsLoggingOut] = useState(false);

  const handleLogout = async () => {
    setIsLoggingOut(true);
    try {
      await logoutApi();
      logout();
      navigate("/login", { replace: true });
    } catch (error) {
      console.error("Logout failed:", error);
    } finally {
      setIsLoggingOut(false);
      setShowConfirmLogoutDialog(false);
    }
  };

  return (
    <div className="sidebar">
      <ul className="sidebar-menu">
        <li>
          <NavLink to="equipments">⚙️ Equipments</NavLink>
        </li>
        <li>
          <NavLink to="requests">📥 Manage Requests</NavLink>
        </li>
      </ul>

      {/* Logout Button at bottom */}
      <div className="sidebar-logout">
        <Button
          variant="outlined"
          color="error"
          onClick={() => setShowConfirmLogoutDialog(true)}
          fullWidth
        >
          Logout
        </Button>
      </div>

      {/* Confirm Logout Dialog */}
      <Dialog
        open={showConfirmLogoutDialog}
        onClose={() => setShowConfirmLogoutDialog(false)}
      >
        <DialogTitle>{"Logout"}</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Are you sure you want to logout?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={handleLogout}
            autoFocus
            color="error"
            disabled={isLoggingOut}
            sx={{
              "&:hover": {
                cursor: isLoggingOut ? "not-allowed" : "pointer",
              },
            }}
          >
            {isLoggingOut ? (
              <CircularProgress size={18} style={{ color: "red" }} />
            ) : (
              "Yes"
            )}
          </Button>
          <Button onClick={() => setShowConfirmLogoutDialog(false)}>No</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
};

export default Sidebar;
