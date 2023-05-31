import React, {useState, useEffect} from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { ShoppingCart, UserCircle } from "phosphor-react";
import "./navbar.css";

export const Navbar = () => {
  const [displayusername, displayusernameupdate] = useState('');
  const [showmenu, showmenuupdateupdate] = useState(false);
  const usenavigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    if (location.pathname === '/login' || location.pathname === '/register') {
        showmenuupdateupdate(false);
    } else {
        showmenuupdateupdate(true);
        let username = sessionStorage.getItem('username');
        if (username === '' || username === null) {
            usenavigate('/login');
        } else {
            displayusernameupdate(username);
        }
    }

  }, [location])

  return (
    <div>{ showmenu &&
      <div className="navbar">
        <div className="links">
          <Link to="/profile" className="user-link">
            <UserCircle size={32} /> {displayusername}
          </Link>
          <Link to="/"> Shop </Link>
          <Link to="/contact"> Contact </Link>
          <Link to="/cart">
            <ShoppingCart size={32} />
          </Link>
          <Link to="/login">Logout</Link>
        </div>
      </div>
      }
    </div>

  );
};