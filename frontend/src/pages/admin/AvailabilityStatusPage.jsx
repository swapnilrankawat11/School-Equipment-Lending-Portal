import { useEffect, useState } from "react";
import { getAvailabilityStatuses } from "../../services/admin/availabilityStatusApi";
import { toast } from "react-toastify";
import AvailabilityStatusList from "../../components/admin/AvailabilityStatusList";
import AvailabilityStatusDetailsModal from "../../components/admin/AvailabilityStatusDetailsModal";
import "../../styles/admin/AvailabilityStatusPage.css";

const AvailabilityStatusPage = () => {
  const [availabilityStatuses, setAvailabilityStatuses] = useState([]);
  const [selectedAvailabilityStatus, setSelectedAvailabilityStatus] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  const fetchAvailabilityStatuses = async () => {
    setIsLoading(true);
    try {
      const response = await getAvailabilityStatuses();
      setAvailabilityStatuses(response.data);
    } catch (err) {
      const errorMsg =
        err?.response?.data?.error || "Something went wrong! Try again.";
      toast.error(errorMsg);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchAvailabilityStatuses();
  }, []);

  const handleViewDetails = (status) => {
    setSelectedAvailabilityStatus(status);
  };

  return (
    <div className="availability-status-page">
      <div className="availability-status-header">
        <h2>Availability Statuses</h2>
      </div>

      {availabilityStatuses && (
        <AvailabilityStatusList
          availabilityStatuses={availabilityStatuses}
          loading={isLoading}
          onViewDetails={handleViewDetails}
        />
      )}

      {selectedAvailabilityStatus && (
        <AvailabilityStatusDetailsModal
          availabilityStatus={selectedAvailabilityStatus}
          onClose={() => setSelectedAvailabilityStatus(null)}
        />
      )}
    </div>
  );
};

export default AvailabilityStatusPage;
