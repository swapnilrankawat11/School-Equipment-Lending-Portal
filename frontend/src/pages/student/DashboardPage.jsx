import { useEffect, useState } from "react";
import { getEquipments } from "../../services/admin/equipmentsApi";
import { toast } from "react-toastify";
import EquipmentList from "../../components/student/EquipmentList";
import EquipmentDetailsModal from "../../components/admin/EquipmentDetailsModal";
import "../../styles/admin/EquipmentsPage.css";
import EquipmentRequestModal from "../../components/student/EquipmentRequestModal";

const EquipmentsPage = () => {
  const [equipments, setEquipments] = useState([]);
  const [selectedEquipment, setSelectedEquipment] = useState(null);
  const [requestedEquipment, setRequestedEquipment] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [showModal, setShowModal] = useState(false);

  const fetchEquipments = async () => {
    setIsLoading(true);
    try {
      const response = await getEquipments();
      setEquipments(response.data);
    } catch (err) {
      const errorMsg =
        err?.response?.data?.error || "Something went wrong! Try again.";
      toast.error(errorMsg);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchEquipments();
  }, []);

  const handleViewDetails = (equipment) => {
    setSelectedEquipment(equipment);
  };

  const handleEquipmentRequests = (equipment) => {
    setRequestedEquipment(equipment);
    setShowModal(true);
  };

  return (
    <div className="equipments-page">
      <div className="equipments-header">
        <h2>Dashboard</h2>
      </div>

      {showModal && (
        <EquipmentRequestModal
          requestedEquipment={requestedEquipment}
          onCancel={() => setShowModal(false)}
        />
      )}

      {equipments && (
        <EquipmentList
          equipments={equipments}
          loading={isLoading}
          onRequest={handleEquipmentRequests}
          onViewDetails={handleViewDetails}
        />
      )}

      {selectedEquipment && (
        <EquipmentDetailsModal
          equipment={selectedEquipment}
          onClose={() => {
            setSelectedEquipment(null);
          }}
        />
      )}
    </div>
  );
};

export default EquipmentsPage;
