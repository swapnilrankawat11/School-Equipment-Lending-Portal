import { createContext, useContext, useState, useEffect } from "react";
import { getSessionInfo } from "../services/auth/sessionInfoApi";
import { toast } from "react-toastify";

const SessionContext = createContext();

export const SessionProvider = ({ children }) => {
  const [sessionInfo, setSessionInfo] = useState(() => {
    const cached = localStorage.getItem("sessionInfo");
    return cached ? JSON.parse(cached) : undefined;
  });
  const [loading, setLoading] = useState(sessionInfo === undefined);
  const [hasLoggedOut, setHasLoggedOut] = useState(false);
  const fetchSessionInfo = async () => {
    try {
      setLoading(true);
      const response = await getSessionInfo();
      setSessionInfo(response.data);
      localStorage.setItem("sessionInfo", JSON.stringify(response.data));
    } catch (err) {
      setSessionInfo(null);
      localStorage.removeItem("sessionInfo");
      const errorMsg =
        err?.response?.data?.error || "Something went wrong! Try Again.";
      toast.error(errorMsg);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchSessionInfo();
  }, []);

  useEffect(() => {
    const interval = setInterval(() => {
      fetchSessionInfo();
    }, 15 * 60 * 1000);
    return () => clearInterval(interval);
  }, []);

  const logout = () => {
    setSessionInfo(null);
    setHasLoggedOut(true);
    localStorage.removeItem("sessionInfo");
  };

  return (
    <SessionContext.Provider
      value={{ sessionInfo, loading, fetchSessionInfo, logout, hasLoggedOut }}
    >
      {children}
    </SessionContext.Provider>
  );
};

// Custom hook
export const useSession = () => useContext(SessionContext);
