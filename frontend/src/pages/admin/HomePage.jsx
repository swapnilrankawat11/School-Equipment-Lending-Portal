import Topbar from "../../components/common/Topbar";
import Sidebar from "../../components/admin/Sidebar";
import MainContent from "../../components/admin/MainContent";
import "../../styles/common/HomePage.css";

const HomePage = () => {
  return (
    <div className="home-wrapper">
      <Topbar />
      <div className="home-container">
        <Sidebar />
        <MainContent />
      </div>
    </div>
  );
};

export default HomePage;