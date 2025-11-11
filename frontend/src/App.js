import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import AdminHomePage from "./pages/admin/HomePage";
import StudentHomePage from "./pages/student/HomePage";
import StaffHomePage from "./pages/staff/HomePage";
import EquipmentsPage from "./pages/admin/EquipmentsPage";
import CategoriesPage from "./pages/admin/CategoriesPage";
import DashboardPage from "./pages/student/DashboardPage";
import StaffEquipmentsPage from "./pages/staff/EquipmentsPage";
import TrackRequestsPage from "./pages/student/TrackRequestsPage";
import AvailabilityStatusPage from "./pages/admin/AvailabilityStatusPage";
import ManageRequestsPage from "./pages/admin/ManageRequestsPage";
import NotFoundPage from "./pages/common/NotFoundPage";
import LoginPage from "./pages/auth/LoginPage";
import SignupPage from "./pages/auth/SignupPage";
import PrivateRoute from "./components/auth/PrivateRoute";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { SessionProvider } from "./contexts/SessionContext";

function App() {
  return (
    <>
      <ToastContainer
        toastStyle={{
          minWidth: "350px",
          height: "50px",
        }}
        position="bottom-left"
        autoClose={5000}
        newestOnTop={true}
      />

      <SessionProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<Navigate to="/login" replace />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/signup" element={<SignupPage />} />

            <Route element={<PrivateRoute allowedRoles={["admin"]} />}>
              <Route path="/admin" element={<AdminHomePage />}>
                <Route path="equipments" element={<EquipmentsPage />} />
                <Route path="categories" element={<CategoriesPage />} />
                <Route
                  path="availabilities"
                  element={<AvailabilityStatusPage />}
                />
                <Route path="requests" element={<ManageRequestsPage />} />
              </Route>
            </Route>

            <Route element={<PrivateRoute allowedRoles={["student"]} />}>
              <Route path="/student" element={<StudentHomePage />}>
                <Route path="dashboard" element={<DashboardPage />} />
                <Route path="requests" element={<TrackRequestsPage />} />
              </Route>
            </Route>

            <Route element={<PrivateRoute allowedRoles={["staff"]} />}>
              <Route path="/staff" element={<StaffHomePage />}>
                <Route path="equipments" element={<StaffEquipmentsPage />} />
                <Route path="requests" element={<ManageRequestsPage />} />
              </Route>
            </Route>

            <Route path="*" element={<NotFoundPage />} />
          </Routes>
        </BrowserRouter>
      </SessionProvider>
    </>
  );
}

export default App;
