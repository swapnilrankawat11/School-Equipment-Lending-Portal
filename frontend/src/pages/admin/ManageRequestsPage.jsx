import { useEffect, useState } from "react";
import {
  getRequestsByStatusType,
  updateRequest,
} from "../../services/admin/requestsApi";
import { getRequestStatusTypes } from "../../services/admin/requestStatusTypesApi";
import { toast } from "react-toastify";
import RequestList from "../../components/admin/RequestsList";
import ApproveRequestModal from "../../components/admin/ApproveRequestModal";
import RequestDetailsModal from "../../components/admin/RequestDetailsModal";
import { FormControl, InputLabel, MenuItem, Select } from "@mui/material";
import "../../styles/admin/ManageRequestsPage.css";

import { useSession } from "../../contexts/SessionContext";
const ManageRequestsPage = () => {
  const [requests, setRequests] = useState([]);
  const [requestStatusTypes, setRequestStatusTypes] = useState([]);
  const [selectedRequest, setSelectedRequest] = useState(null);
  const [managingRequest, setManagingRequest] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const { sessionInfo } = useSession();
  const [statusFilter, setStatusFilter] = useState({
    id: "",
    name: "",
  });

  const fetchStatusTypes = async () => {
    try {
      const res = await getRequestStatusTypes();
      setRequestStatusTypes(res.data);

      if (res.data.length > 0) {
        setStatusFilter({
          id: res.data[0].id,
          name: res.data[0].status_type,
        });
      }
    } catch (err) {
      toast.error("Failed to load request status types.");
    }
  };

  const fetchRequests = async () => {
    if (!statusFilter.id) return;
    setIsLoading(true);
    try {
      const response = await getRequestsByStatusType(statusFilter.id);
      setRequests(response.data);
    } catch (err) {
      const errorMsg =
        err?.response?.data?.error || "Something went wrong! Try again.";
      toast.error(errorMsg);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchStatusTypes();
  }, []);

  useEffect(() => {
    if (statusFilter.id) fetchRequests();
  }, [statusFilter.id]);

  const handleManage = (request) => setManagingRequest(request);
  const handleViewDetails = (request) => setSelectedRequest(request);
  const handleSuccess = () => fetchRequests();

  const handleCloseRequest = async (request) => {
    try {
      const payload = {
        requestStatusId: Number(
          requestStatusTypes.find(
            (st) => st.status_type.toLowerCase() === "closed"
          ).id
        ),
        modifiedBy: sessionInfo?.userId || undefined,
      };
      // Remove undefined keys
      Object.keys(payload).forEach(
        (key) => payload[key] === undefined && delete payload[key]
      );
      const response = await updateRequest(request.id, payload);

      if (response.data.success === true) {
        toast.success("Request closed successfully!");
        handleSuccess();
      } else {
        toast.error("Failed to close the request.");
      }
    } catch (err) {
      toast.error("Failed to close the request.");
    }
  };

  const handleRejectRequest = async (request) => {
    try {
      const payload = {
        requestStatusId: Number(
          requestStatusTypes.find(
            (st) => st.status_type.toLowerCase() === "rejected"
          ).id
        ),
        modifiedBy: sessionInfo?.userId || undefined,
      };
      // Remove undefined keys
      Object.keys(payload).forEach(
        (key) => payload[key] === undefined && delete payload[key]
      );
      const response = await updateRequest(request.id, payload);

      if (response.data.success === true) {
        toast.success("Request rejected successfully!");
        handleSuccess();
      } else {
        toast.error("Failed to reject the request.");
      }
    } catch (err) {
      toast.error("Failed to reject the request.");
    }
  };

  const handleStatusChange = (event) => {
    const selectedId = event.target.value;
    const selectedType = requestStatusTypes.find((st) => st.id === selectedId);

    setStatusFilter({
      id: selectedType.id,
      name: selectedType.status_type,
    });
  };

  return (
    <div className="manage-requests-page">
      <div className="manage-requests-header">
        <h2>Manage Equipment Requests</h2>

        <FormControl size="small" sx={{ minWidth: 180, marginLeft: "auto" }}>
          <InputLabel>Status</InputLabel>
          <Select
            value={statusFilter.id}
            label="Status"
            onChange={handleStatusChange}
          >
            {requestStatusTypes.map((st) => (
              <MenuItem key={st.id} value={st.id}>
                {st.status_type}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      </div>

      <RequestList
        requests={requests}
        loading={isLoading}
        onManage={handleManage}
        onClose={handleCloseRequest}
        onReject={handleRejectRequest}
        onViewDetails={handleViewDetails}
        statusType={statusFilter}
      />

      {managingRequest && (
        <ApproveRequestModal
          selectedRequest={managingRequest}
          onSuccess={handleSuccess}
          onCancel={() => setManagingRequest(null)}
        />
      )}

      {selectedRequest && (
        <RequestDetailsModal
          request={selectedRequest}
          onClose={() => setSelectedRequest(null)}
        />
      )}
    </div>
  );
};

export default ManageRequestsPage;
