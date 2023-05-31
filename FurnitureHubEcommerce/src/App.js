import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { Navbar } from "./components/navbar";
import { Shop } from "./pages/shop/shop";
import { Contact } from "./pages/contact";
import { Cart } from "./pages/cart/cart";
import { ShopContextProvider } from "./context/shop-context";
import Login from './Login';
import Register from './Register';
import { ToastContainer } from 'react-toastify';
import { Profile } from "./pages/profile/profile";

function App() {
  return (
    <div className="App">
      <ToastContainer theme='colored' position='top-center'></ToastContainer>
      <ShopContextProvider>
        <Router>
          <Navbar />
          <Routes>
            <Route path='/login' element={<Login/>} />
  
            <Route path="/" element={<Shop />} />
            <Route path='/register' element={<Register/>} />
            <Route path="/contact" element={<Contact />} />
            <Route path="/cart" element={<Cart />} />
            <Route path="/profile" element={<Profile /> } />
          </Routes>
        </Router>
      </ShopContextProvider>
    </div>
  );
}

export default App;
