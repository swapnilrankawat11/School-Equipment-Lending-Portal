import { useEffect, useState } from "react";
import { getEquipments, deleteEquipment } from "../../services/admin/equipmentsApi";
import { toast } from "react-toastify";
import EquipmentList from "../../components/admin/EquipmentList";
import EquipmentDetailsModal from "../../components/admin/EquipmentDetailsModal";
import AddEditEquipmentModal from "../../components/admin/AddEditEquipmentModal";
import "../../styles/admin/EquipmentsPage.css";

const EquipmentsPage = () => {
  const [equipments, setEquipments] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [editingEquipmentDetails, setEditingEquipmentDetails] = useState(null);
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

  const handleDelete = async (id) => {
    try {
      await deleteEquipment(id);
      toast.success("Equipment deleted successfully!");
      fetchEquipments();
    } catch (err) {
      toast.error("Failed to delete equipment.");
    }
  };

  const handleEdit = (item) => {
    setEditingEquipmentDetails(item);
    setShowModal(true);
  };

  const handleSuccess = () => {
    fetchEquipments();
  };

  const handleViewDetails = (equipment) => {
    setSelectedEquipment(equipment);
  };

  return (
    <div className="equipments-page">
      <div className="equipments-header">
        <h2>Manage Equipments</h2>
        <button
          className="add-equipment-button"
          onClick={() => {
            setEditingEquipmentDetails(null);
            setShowModal(true);
          }}
        >
          + Add Equipment
        </button>
      </div>

      {showModal && (
        <AddEditEquipmentModal
          editingEquipmentDetails={editingEquipmentDetails}
          onSuccess={handleSuccess}
          onCancel={() => {
            setEditingEquipmentDetails(null);
            setShowModal(false);
          }}
        />
      )}

      {equipments && (
        <EquipmentList
          equipments={equipments}
          loading={isLoading}
          onEdit={handleEdit}
          onDelete={handleDelete}
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
