import "../../styles/common/Topbar.css";
import { useSession } from "../../contexts/SessionContext";

const Topbar = () => {
  const { sessionInfo, loading } = useSession();

  const avatarText = sessionInfo
    ? `${sessionInfo.firstName?.[0] || ""}${
        sessionInfo.lastName?.[0] || ""
      }`.toUpperCase()
    : "";

  return (
    <div className="topbar">
      <div className="topbar-left">
        <div className="logo">School Equipment Lending Portal</div>
      </div>
      <div className="topbar-right">
        <div className="topbar-user">{loading ? "..." : avatarText}</div>
      </div>
    </div>
  );
};

export default Topbar;
