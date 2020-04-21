import React, { useState, useEffect } from 'react';
import Orders from "./Orders";
import Header from "./Header";
import https from 'https';
import axios from 'axios';

function Body(props){

  const [getUrl, setGetUrl] = useState('http://50.19.176.137:8000/orders/123');

  useEffect(() => {
    if(props.path === '/active'){
      setGetUrl('http://50.19.176.137:8000/orders/123');
    } else if (props.path === '/completed'){
      setGetUrl('http://50.19.176.137:8000/orders/complete/123');
    }
  }, [props.path]);

  useEffect(() => {
    updateOrders();
  }, [getUrl]);

  useEffect(() => {
    // updates orders every 10 seconds
    const interval = setInterval(updateOrders, 10000); // start interval after mounting
    return () => clearInterval(interval); // clear interval after unmounting
  }, []);

  // Get In Progress orders from database
  function updateOrders(){
    console.log('updating orders');
    serverRequest(
      'get',
      getUrl,
      '',
      configureOrders
    );
  }

  const [orders, setOrders] = useState({});

  function configureOrders(orders){
    // console.log(orders);
    let ordersState = {};
    if(orders === undefined){
      console.log('no orders');
      return;
    }
    orders = orders.data;
    // Iterate over each order
    Object.values(orders).forEach(order => {
      // console.log(order);
      // Check if that order_num exists
      if(!(order.order_num in ordersState)){
        // Create new order with empty items
        ordersState[order.order_num] = {order_num: order.order_num, table: order.table, order_date: order.order_date, items: {}, expand: false};
      }
      // If no category provided, mark as Entree
      if(!order.category){
        order.category = 'Entrees';
      }
      if(!(order.category in ordersState[order.order_num].items)){
        ordersState[order.order_num].items[order.category] = [];
      }
      // Add item to order
      ordersState[order.order_num].items[order.category].push({quantity: order.quantity, title: order.item_name});
    });
    setOrders(ordersState);
  }

  const [selectedOrder, setSelectedOrder] = useState(0);

  function changeSelectedOrder(cardId){
    setSelectedOrder(cardId);
  }

  function changeOrderStatus(status){
    const orderNum = Object.keys(orders)[selectedOrder];
    serverRequest(
      'post',
      'http://50.19.176.137:8000/orders/update',
      `order_num=${orderNum}&order_status=${status}`,
      () => {
        updateOrders(); // grab orders from database
        changeSelectedOrder(0);
      }
    );
  }

  function serverRequest(method, url, data, responseCallback){
    axios({
      method: method,
      url: url,
      data: data,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      httpsAgent: new https.Agent({
        rejectUnauthorized: false,
      }),
    })
      .then((res) => {
        responseCallback(res);
      })
      .catch(error =>{
        alert('server error');
        console.error(error);
      });
  }

  return (
    <div className="p-3">
      <Header handleStatusChangeClick={changeOrderStatus} path={props.path} />
      {/*<Header handleExpandClick={this.toggleExpandOrder.bind(this)} handleCompleteClick={markOrderComplete} />*/}
      <Orders orders={orders} selectedOrder={selectedOrder} handleCardClick={changeSelectedOrder} />
    </div>
  )
}

export default Body;