// handles logic of adding and removing, updating cart for the user

import { createContext, useEffect, useState } from "react";
import FurnitureApi from "../apis/FurnitureApi";

export const ShopContext = createContext(null);

const getDefaultCart = (furnitureList) => {
  let cart = {};
  for (let i = 1; i < furnitureList.length + 1; i++) {
    cart[i] = 0;
  }
  return cart;
};

export const ShopContextProvider = (props) => {
  const [furnitureList, setFurnitureList] = useState([]);
  useEffect( () =>  {
      FurnitureApi.getFurniture(setFurnitureList);
  }, [] );

  const [cartItems, setCartItems] = useState(getDefaultCart(furnitureList));

  const getTotalCartAmount = () => {
    let totalAmount = 0;
    for (const item in cartItems) {
      if (cartItems[item] > 0) {
        let itemInfo = furnitureList.find((product) => product.id === Number(item));
        totalAmount += cartItems[item] * itemInfo.price;
      }
    }
    return totalAmount;
  };

  const addToCart = (itemId) => {
    setCartItems((prev) => ({ ...prev, [itemId]: prev[itemId] + 1 }));
  };

  const removeFromCart = (itemId) => {
    setCartItems((prev) => ({ ...prev, [itemId]: prev[itemId] - 1 }));
  };

  const updateCartItemCount = (newAmount, itemId) => {
    setCartItems((prev) => ({ ...prev, [itemId]: newAmount }));
  };

  const checkout = () => {
    setCartItems(getDefaultCart(furnitureList));
  };

  const contextValue = {
    cartItems,
    addToCart,
    updateCartItemCount,
    removeFromCart,
    getTotalCartAmount,
    checkout,
  };

  return (
    <ShopContext.Provider value={contextValue}>
      {props.children}
    </ShopContext.Provider>
  );
};