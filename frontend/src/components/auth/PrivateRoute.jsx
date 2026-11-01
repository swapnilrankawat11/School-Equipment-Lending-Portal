import { useEffect, useState } from "react";
import { Outlet, Navigate } from "react-router-dom";
import { useSession } from "../../contexts/SessionContext";
import NotFoundPage from "../../pages/common/NotFoundPage";
import { toast } from "react-toastify";

const PrivateRoute = ({ allowedRoles }) => {
  const { sessionInfo, loading, hasLoggedOut } = useSession();
  const [redirect, setRedirect] = useState(false);

  useEffect(() => {
    if (
      !loading &&
      !hasLoggedOut &&
      (!sessionInfo || !sessionInfo.userId || !sessionInfo.userTypeName)
    ) {
      toast.error("Session expired! Redirecting to login in 10 seconds...", {
        autoClose: 10000,
      });

      const timer = setTimeout(() => setRedirect(true), 10000);
      return () => clearTimeout(timer);
    }
  }, [loading, sessionInfo]);

  if (loading) return <div>Loading...</div>;

  if (!sessionInfo || !sessionInfo.userId || !sessionInfo.userTypeName) {
    if (redirect) return <Navigate to="/login" replace />;
    return <div>Session expired. You will be redirected shortly...</div>;
  }

  const userRole = sessionInfo.userTypeName.toLowerCase();

  if (!allowedRoles.map((role) => role.toLowerCase()).includes(userRole)) {
    return <NotFoundPage />;
  }

  return <Outlet />;
};

export default PrivateRoute;
