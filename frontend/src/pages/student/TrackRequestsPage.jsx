import { useEffect, useState } from "react";
import RequestList from "../../components/admin/RequestsList";
import RequestDetailsModal from "../../components/admin/RequestDetailsModal";

import { useSession } from "../../contexts/SessionContext";
import {
  getRequestsByStudent,
  cancelRequest,
} from "../../services/student/trackRequestsApi";
import "../../styles/student/TrackRequestsPage.css";

import { toast } from "react-toastify";
import { getRequestStatusTypes } from "../../services/admin/requestStatusTypesApi";
import { FormControl, InputLabel, MenuItem, Select } from "@mui/material";

const TrackRequestsPage = () => {
  const { sessionInfo } = useSession();
  const [requests, setRequests] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [selectedRequest, setSelectedRequest] = useState(null);
  const [requestStatusTypes, setRequestStatusTypes] = useState([]);
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
    if (!sessionInfo || !statusFilter.id) return;
    setIsLoading(true);
    try {
      const res = await getRequestsByStudent(
        sessionInfo.userId,
        statusFilter.id
      );
      setRequests(res.data);
    } catch (err) {
      toast.error(err?.response?.data?.error || "Failed to fetch requests.");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchStatusTypes();
  }, []);

  useEffect(() => {
    fetchRequests();
  }, [sessionInfo, statusFilter.id]);

  const handleCancelRequest = async (request) => {
    try {
      await cancelRequest(request.id);
      toast.success("Request canceled successfully!");
      fetchRequests();
    } catch (err) {
      toast.error(err?.response?.data?.error || "Failed to cancel request.");
    }
  };

  const handleViewDetails = (request) => setSelectedRequest(request);

  const handleStatusChange = (event) => {
    const selectedId = event.target.value;
    const selectedType = requestStatusTypes.find((st) => st.id === selectedId);

    setStatusFilter({
      id: selectedType.id,
      name: selectedType.status_type,
    });
  };

  return (
    <div className="track-requests-page">
      <div
        className="track-requests-header"
        style={{ display: "flex", alignItems: "center", gap: "20px" }}
      >
        <h2>Track Equipment Requests</h2>
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
        onCancel={handleCancelRequest}
        onViewDetails={handleViewDetails} // Pass handler to RequestList
        studentView={true}
      />

      {/* Request Details Modal */}
      {selectedRequest && (
        <RequestDetailsModal
          request={selectedRequest}
          onClose={() => setSelectedRequest(null)}
        />
      )}
    </div>
  );
};

export default TrackRequestsPage;
