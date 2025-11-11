import { useEffect, useState } from "react";
import { getEquipments } from "../../services/admin/equipmentsApi";
import EquipmentList from "../../components/admin/EquipmentList";
import { toast } from "react-toastify";
import EquipmentDetailsModal from "../../components/admin/EquipmentDetailsModal";
import "../../styles/admin/EquipmentsPage.css";

const DashboardPage = () => {
  const [equipments, setEquipments] = useState([]);
  const [selectedEquipment, setSelectedEquipment] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

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

  return (
    <div className="equipments-page">
      <div className="equipments-header">
        <h2>Equipments</h2>
      </div>

      {equipments && (
        <EquipmentList
          role={"staff"}
          equipments={equipments}
          loading={isLoading}
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

export default DashboardPage;
