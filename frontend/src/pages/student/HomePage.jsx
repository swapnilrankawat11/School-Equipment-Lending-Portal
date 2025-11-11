import Topbar from "../../components/common/Topbar";
import Sidebar from "../../components/student/Sidebar";
import MainContent from "../../components/student/MainContent";
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