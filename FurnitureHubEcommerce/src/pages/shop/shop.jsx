import React, {useState, useEffect} from "react";
import FurnitureApi from "../../apis/FurnitureApi";
import { Product } from "./product";
import "./shop.css";

export const Shop = () => {
    const [furnitureList, setFurnitureList] = useState([]);
    useEffect( () =>  {
        FurnitureApi.getFurniture(setFurnitureList);
    }, [] );
    
  return (
    <div className="shop">
      <div className="shopTitle">
        <h1>Jesus's Furniture Shop</h1>
      </div>

      <div className="products">
        {furnitureList.map((product) => (
          <Product key={product.id} data={product} />
        ))}
      </div>
    </div>
  );
};