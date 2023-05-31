import React, {useState, useEffect} from 'react'
import UserApi from '../../apis/UserApi';
import "./profile.css"

export const Profile = () => {
    let userId = sessionStorage.getItem('id');
    console.log(userId);

    const [userList, setUserList] = useState([]);
    useEffect( () =>  {
        UserApi.getUserById(setUserList, userId);
    }, [] );

    console.log(userList.previousOrders);

    return (
        <div className="profile">
            <div className="profileTitle">
                <h2>User Account Information:</h2>
            </div>
        
            <div className="userInfo">
                <p>
                    <b>Username:</b> {userList.username}
                </p>
                <p>
                    <b>Name:</b> {userList.first_name} {userList.last_name}
                </p>
                <p>
                    <b>Email:</b> {userList.email}
                </p>
                <p>
                    <b>Telephone:</b> {userList.telephone}
                </p>
                <div className="previousOrders">
                    <p>
                        <b>Previous Orders:</b>
                    </p>
                    <table>
                        <thead>
                            <tr>
                            <th>Order ID</th>
                            <th>Date</th>
                            <th>Items</th>
                            <th>Total</th>
                            </tr>
                        </thead>
                        <tbody>
                            {userList.previousOrders?.map((order) => (
                            <tr key={order.order_id}>
                                <td>{order.order_id}</td>
                                <td>{order.date}</td>
                                <td>
                                <ul>
                                    {order.items?.map((item) => (
                                    <li key={item.id}>
                                        {item.name} ({item.quantity}) - ${item.price}
                                    </li>
                                    ))}
                                </ul>
                                </td>
                                <td>${order.total}</td>
                            </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>    
    )
}
