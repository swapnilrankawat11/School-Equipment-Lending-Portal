import { useEffect, useState } from "react";
import { getCategories, deleteCategory } from "../../services/admin/categoriesApi";
import { toast } from "react-toastify";
import CategoryList from "../../components/admin/CategoryList";
import CategoryDetailsModal from "../../components/admin/CategoryDetailsModal";
import AddEditCategoryModal from "../../components/admin/AddEditCategoryModal";
import "../../styles/admin/CategoryPage.css";

const CategoriesPage = () => {
  const [categories, setCategories] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [editingCategoryDetails, setEditingCategoryDetails] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState(null);

  const fetchCategories = async () => {
    setIsLoading(true);
    try {
      const response = await getCategories();
      setCategories(response.data);
    } catch (err) {
      const errorMsg =
        err?.response?.data?.error || "Something went wrong! Try again.";
      toast.error(errorMsg);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  // For delete functionality
  const handleDelete = async (id) => {
    try {
      await deleteCategory(id);
      toast.success("Category deleted successfully!");
      fetchCategories();
    } catch (err) {
      toast.error("Failed to delete category.");
    }
  };

  // For edit functionality
  const handleEdit = (item) => {
    setEditingCategoryDetails(item);
    setShowModal(true);
  };

  // For refresh after add/edit
  const handleSuccess = () => {
    fetchCategories();
  };

  // For view details functionality
  const handleViewDetails = (category) => {
    setSelectedCategory(category);
  };

  return (
    <div className="categories-page">
      <div className="categories-header">
        <h2>Categories Types</h2>
        <button
          className="add-category-button"
          onClick={() => {
            setEditingCategoryDetails(null);
            setShowModal(true);
          }}
        >
          + Add Category
        </button>
      </div>

      {showModal && (
        <AddEditCategoryModal
          editingCategoryDetails={editingCategoryDetails}
          onSuccess={handleSuccess}
          onCancel={() => {
            setEditingCategoryDetails(null);
            setShowModal(false);
          }}
        />
      )}

      {categories && (
        <CategoryList
          categories={categories}
          loading={isLoading}
          onEdit={handleEdit}
          onDelete={handleDelete}
          onViewDetails={handleViewDetails}
        />
      )}

      {selectedCategory && (
        <CategoryDetailsModal
          category={selectedCategory}
          onClose={() => setSelectedCategory(null)}
        />
      )}
    </div>
  );
};

export default CategoriesPage;
