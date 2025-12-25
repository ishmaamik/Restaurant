# 🧪 Complete Postman Testing Guide - Restaurant System

## 📋 Prerequisites

1. **Backend running** on `http://localhost:8080`
2. **Postman** installed and ready
3. **WebSocket HTML client** (optional, for real-time notifications)

---

## 🔐 **PHASE 1: Authentication & User Management**

### **1.1 Register New User** (Public - No Auth Required)

```
POST http://localhost:8080/api/users/register
Content-Type: application/json

Body (raw JSON):
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123"
}
```

**Expected Response (201):**
```json
{
  "userId": "92f02e14-8f78-42fc-9173-1d44f00189e4",
  "username": "testuser",
  "email": "test@example.com",
  "roles": ["CUSTOMER"]
}
```

**Save:** Copy `userId` for later use

---

### **1.2 Login** (Public - No Auth Required)

```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

Body (raw JSON):
{
  "username": "testuser",
  "password": "password123"
}
```

**Expected Response (200):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Save:** Copy `accessToken` - you'll need this for all authenticated requests!

**Postman Tip:** Create an environment variable `{{accessToken}}` and set it here.

---

### **1.3 Refresh Token** (Public - No Auth Required)

```
POST http://localhost:8080/api/auth/refresh
Content-Type: application/json

Body (raw JSON):
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Expected Response (200):** New access and refresh tokens

---

## 🍽️ **PHASE 2: Menu Management** (Admin Only)

### **2.1 Get All Menus** (Public - No Auth Required)

```
GET http://localhost:8080/api/menus/all
```

**Expected Response (200):** Array of menu items

---

### **2.2 Get Single Menu Item** (Public - No Auth Required)

```
GET http://localhost:8080/api/menus/{menuId}
```

**Replace `{menuId}`** with actual UUID from step 2.1

**Expected Response (201):**
```json
{
  "menuId": "...",
  "name": "Burger",
  "price": 15.99,
  "active": true,
  "category": "MAIN"
}
```

---

### **2.3 Create Menu Item** (Admin Only)

```
POST http://localhost:8080/api/menus
Authorization: Bearer {{accessToken}}
Content-Type: application/json

Body (raw JSON):
{
  "name": "Cheeseburger",
  "price": 18.50,
  "active": true,
  "category": "MAIN"
}
```

**Note:** Requires ADMIN role. If you get 403, you need to assign ADMIN role to your user in database.

**Expected Response (200):** Created menu item with `menuId`

**Save:** Copy `menuId` for order creation

---

### **2.4 Update Menu Price** (Admin Only)

```
POST http://localhost:8080/api/menus/{menuId}/price
Authorization: Bearer {{accessToken}}
Content-Type: application/json

Body (raw JSON):
{
  "price": 20.00
}
```

**Expected Response (200):** Updated menu item

---

### **2.5 Activate Menu Item** (Admin Only)

```
POST http://localhost:8080/api/menus/{menuId}/activate
Authorization: Bearer {{accessToken}}
```

**Expected Response (200):** Menu item with `active: true`

---

### **2.6 Deactivate Menu Item** (Admin Only)

```
POST http://localhost:8080/api/menus/{menuId}/deactivate
Authorization: Bearer {{accessToken}}
```

**Expected Response (200):** Menu item with `active: false`

---

## 🛒 **PHASE 3: Order Management**

### **3.1 Create Order** (Authenticated)

```
POST http://localhost:8080/api/orders/create
Authorization: Bearer {{accessToken}}
Content-Type: application/json

Body (raw JSON):
{
  "userId": "92f02e14-8f78-42fc-9173-1d44f00189e4",
  "tableNo": 5
}
```

**Expected Response (200):**
```json
{
  "userId": "92f02e14-8f78-42fc-9173-1d44f00189e4",
  "tableNo": 5
}
```

**Save:** Copy `orderId` from response (if present) or get it from step 3.2

**WebSocket:** If connected, you should see message on `/topic/table/5`

---

### **3.2 Get Customer Orders** (Public - No Auth Required)

```
GET http://localhost:8080/api/orders/{userId}/all
```

**Replace `{userId}`** with your user ID from registration

**Expected Response (200):** Array of orders for that customer

**Save:** Copy `orderId` from the response

---

### **3.3 Add Item to Order** (Authenticated)

```
POST http://localhost:8080/api/orders/{orderId}/add
Authorization: Bearer {{accessToken}}
Content-Type: application/json

Body (raw JSON):
{
  "menuId": "your-menu-id-from-step-2.1"
}
```

**Expected Response (200):** Updated order with new item added

**Repeat:** Add multiple items to test order

---

### **3.4 Remove Item from Order** (Authenticated)

```
POST http://localhost:8080/api/orders/{orderId}/remove
Authorization: Bearer {{accessToken}}
Content-Type: application/json

Body (raw JSON):
{
  "menuId": "menu-id-to-remove"
}
```

**Expected Response (200):** Updated order with item removed

---

### **3.5 Confirm Order** (Customer or Admin)

```
POST http://localhost:8080/api/orders/{orderId}/confirm
Authorization: Bearer {{accessToken}}
```

**Expected Response (200):** Order with status `CONFIRMED`

**WebSocket Notifications:**
- `/topic/kitchen/new-orders` - Kitchen receives full order details
- `/topic/table/5` - Table receives status update
- `/user/{username}/queue/order-status` - Customer receives personal notification

---

### **3.6 Change Order Status** (Admin, Waiter, Cook, or Cashier)

```
POST http://localhost:8080/api/orders/{orderId}/change-status
Authorization: Bearer {{accessToken}}
Content-Type: application/json

Body (raw JSON):
{
  "orderStatus": "PREPARING"
}
```

**Valid Status Values:**
- `CREATED`
- `CONFIRMED`
- `PREPARING`
- `READY`
- `SERVED`
- `PAID`
- `CANCELLED`
- `PAYMENT_FAILED`

**Expected Response (200):** Updated order with new status

**WebSocket:** Status changes trigger notifications to relevant topics

---

### **3.7 Get Cashier Receipt** (Public - No Auth Required)

```
GET http://localhost:8080/api/orders/{orderId}/receipt
```

**Expected Response (200):** Detailed receipt for cashier view

---

### **3.8 Get Customer Receipt** (Public - No Auth Required)

```
GET http://localhost:8080/api/orders/{orderId}/user-receipt
```

**Expected Response (200):** Customer-friendly receipt

---

## 🍴 **PHASE 4: Order Item Management**

### **4.1 Increment Item Quantity** (Authenticated)

```
POST http://localhost:8080/order-item/{orderItemId}/increment
Authorization: Bearer {{accessToken}}
```

**Expected Response (200):** Updated order item with incremented quantity

---

### **4.2 Decrement Item Quantity** (Authenticated)

```
POST http://localhost:8080/order-item/{orderItemId}/decrement
Authorization: Bearer {{accessToken}}
```

**Expected Response (200):** Updated order item with decremented quantity

---

### **4.3 Prepare Item** (Cook Only)

```
POST http://localhost:8080/order-item/{orderItemId}/prepare
Authorization: Bearer {{accessToken}}
```

**Note:** Requires COOK role

**Expected Response (200):** Item status changed to PREPARING

---

### **4.4 Mark Item Ready** (Cook Only)

```
POST http://localhost:8080/order-item/{orderItemId}/ready
Authorization: Bearer {{accessToken}}
```

**Note:** Requires COOK role

**Expected Response (200):** Item status changed to READY

**WebSocket:** Sends notification to `/topic/waiters/ready-orders`

---

### **4.5 Serve Item** (Waiter Only)

```
POST http://localhost:8080/order-item/{orderItemId}/serve
Authorization: Bearer {{accessToken}}
```

**Note:** Requires WAITER role

**Expected Response (200):** Item status changed to SERVED

---

### **4.6 Cancel Item** (Waiter, Customer, or Admin)

```
POST http://localhost:8080/order-item/{orderItemId}/cancel
Authorization: Bearer {{accessToken}}
```

**Expected Response (200):** Item status changed to CANCELLED

---

## 💳 **PHASE 5: Payment Processing**

### **5.1 Process Payment** (Authenticated)

```
POST http://localhost:8080/payment/{orderId}/pay
Authorization: Bearer {{accessToken}}
Content-Type: application/json

Body (raw JSON):
{
  "userId": "92f02e14-8f78-42fc-9173-1d44f00189e4",
  "orderId": "order-id-here",
  "paymentMethod": "CASH",
  "amount": 25.50,
  "phoneNumber": "optional-for-mobile-payments",
  "cardToken": "optional-for-card-payments"
}
```

**Payment Methods:**
- `CASH`
- `BKASH`
- `NAGAD`
- `CARD`

**Expected Response (200):**
```json
{
  "paymentId": "...",
  "orderId": "...",
  "amount": 25.50,
  "paymentMethod": "CASH",
  "status": "SUCCESS",
  "transactionId": "..."
}
```

**WebSocket:** Payment status changes trigger notifications

---

## 🔌 **PHASE 6: WebSocket Testing**

### **6.1 Setup WebSocket Client**

1. Open the HTML WebSocket test file in browser
2. Paste your JWT `accessToken` (without "Bearer" prefix)
3. Click "Connect"
4. Should see: ✅ Connected to WebSocket

### **6.2 Subscribe to Topics**

Click subscription buttons:
- **Kitchen Orders:** `/topic/kitchen/new-orders`
- **Waiter Alerts:** `/topic/waiters/ready-orders`
- **My Orders:** `/user/queue/order-status`
- **Table 5:** `/topic/table/5`

### **6.3 Trigger WebSocket Notifications**

**Test Flow:**

1. **Create Order** → Should see message on `/topic/table/5`
   ```
   POST /api/orders/create
   ```

2. **Confirm Order** → Should see messages on:
   - `/topic/kitchen/new-orders` (full order details)
   - `/topic/table/5` (status update)
   - `/user/queue/order-status` (personal notification)
   ```
   POST /api/orders/{orderId}/confirm
   ```

3. **Change Status to PREPARING** → Should see on:
   - `/topic/kitchen/orders`
   - `/topic/waiters/orders`
   - `/topic/table/5`
   - `/user/queue/order-status`
   ```
   POST /api/orders/{orderId}/change-status
   Body: {"orderStatus": "PREPARING"}
   ```

4. **Change Status to READY** → Should see on:
   - `/topic/waiters/ready-orders` (special ready event)
   - `/topic/kitchen/orders`
   - `/topic/table/5`
   - `/user/queue/order-status`
   ```
   POST /api/orders/{orderId}/change-status
   Body: {"orderStatus": "READY"}
   ```

5. **Mark Item Ready** (Cook) → Should see on `/topic/waiters/ready-orders`
   ```
   POST /order-item/{orderItemId}/ready
   ```

6. **Process Payment** → Should see on:
   - `/topic/admin/orders`
   - `/topic/cashier/pending-payments`
   - `/topic/table/5`
   ```
   POST /payment/{orderId}/pay
   ```

---

## 📊 **Complete Test Scenarios**

### **Scenario 1: Full Customer Journey**

1. ✅ Register user
2. ✅ Login → Get token
3. ✅ Get all menus
4. ✅ Create order
5. ✅ Add items to order (repeat 2-3 times)
6. ✅ Confirm order
7. ✅ Get customer receipt
8. ✅ Process payment

**Expected WebSocket Messages:**
- Order created → `/topic/table/{tableNo}`
- Order confirmed → `/topic/kitchen/new-orders`, `/topic/table/{tableNo}`, `/user/queue/order-status`
- Payment processed → `/topic/admin/orders`, `/topic/table/{tableNo}`

---

### **Scenario 2: Kitchen Workflow** (Requires COOK role)

1. ✅ Login as cook
2. ✅ Subscribe to `/topic/kitchen/new-orders` in WebSocket client
3. ✅ Customer confirms order (from Scenario 1)
4. ✅ See order in kitchen notifications
5. ✅ Mark items as preparing
6. ✅ Mark items as ready

**Expected WebSocket Messages:**
- Order confirmed → `/topic/kitchen/new-orders` (full order details)
- Items preparing → `/topic/kitchen/orders`
- Items ready → `/topic/waiters/ready-orders`

---

### **Scenario 3: Waiter Workflow** (Requires WAITER role)

1. ✅ Login as waiter
2. ✅ Subscribe to `/topic/waiters/ready-orders` in WebSocket client
3. ✅ Cook marks items ready (from Scenario 2)
4. ✅ See ready orders notification
5. ✅ Serve items
6. ✅ Order status changes to SERVED

**Expected WebSocket Messages:**
- Items ready → `/topic/waiters/ready-orders`
- Order served → `/topic/admin/orders`, `/topic/table/{tableNo}`

---

### **Scenario 4: Admin Management**

1. ✅ Login as admin
2. ✅ Create menu items
3. ✅ Update menu prices
4. ✅ Activate/deactivate menu items
5. ✅ View all orders
6. ✅ Change order statuses
7. ✅ View cashier receipts

---

## 🐛 **Troubleshooting**

### **Problem 1: 403 Forbidden**

**Causes:**
- Missing or invalid JWT token
- Token expired (check `security.jwt.access-validity-secs` in `application.properties`)
- Insufficient role permissions

**Fix:**
- Get fresh token via `/api/auth/login`
- Check SecurityConfig for endpoint permissions
- Verify user has required role

---

### **Problem 2: 401 Unauthorized**

**Causes:**
- No Authorization header
- Invalid token format

**Fix:**
- Add header: `Authorization: Bearer {your-token}`
- Ensure token starts with `eyJ` (JWT format)
- Don't include quotes around token

---

### **Problem 3: 404 Not Found**

**Causes:**
- Wrong endpoint URL
- Missing path variables
- Resource doesn't exist

**Fix:**
- Check endpoint URL matches exactly
- Replace path variables with actual UUIDs
- Verify resource exists in database

---

### **Problem 4: 400 Bad Request**

**Causes:**
- Invalid JSON format
- Missing required fields
- Wrong data types

**Fix:**
- Validate JSON syntax
- Check DTO structure matches exactly
- Ensure UUIDs are valid format
- Check enum values match exactly (case-sensitive)

---

### **Problem 5: No WebSocket Messages**

**Causes:**
- Not subscribed to correct topic
- Order status change doesn't trigger notification
- WebSocket connection lost

**Fix:**
- Verify subscription in browser console
- Check `OrderService` calls `notificationService.notifyStatusChange()`
- Reconnect WebSocket
- Check server logs for errors

---

### **Problem 6: userId is null in CreateOrderDTO response**

**Cause:** MapStruct mapping issue

**Fix:** Add `@Mapping` annotation in `OrderMapper`:
```java
@Mapping(source = "orderedBy.userId", target = "userId")
CreateOrderDTO toCreateOrderDTO(Order order);
```

---

## 🎯 **Quick Reference: All Endpoints**

### **Public Endpoints (No Auth)**
- `POST /api/users/register`
- `POST /api/auth/login`
- `POST /api/auth/refresh`
- `GET /api/menus/all`
- `GET /api/menus/{id}`
- `GET /api/orders/{userId}/all`
- `GET /api/orders/{orderId}/receipt`
- `GET /api/orders/{orderId}/user-receipt`

### **Authenticated Endpoints (Any User)**
- `POST /api/orders/create`
- `POST /api/orders/{orderId}/add`
- `POST /api/orders/{orderId}/remove`
- `POST /order-item/{orderItemId}/increment`
- `POST /order-item/{orderItemId}/decrement`
- `POST /payment/{orderId}/pay`

### **Customer Endpoints**
- `POST /api/orders/{orderId}/confirm`
- `POST /order-item/{orderItemId}/cancel`

### **Admin Endpoints**
- `POST /api/menus`
- `POST /api/menus/{id}/price`
- `POST /api/menus/{id}/activate`
- `POST /api/menus/{id}/deactivate`
- `POST /api/orders/{orderId}/change-status`

### **Cook Endpoints**
- `POST /order-item/{orderItemId}/prepare`
- `POST /order-item/{orderItemId}/ready`

### **Waiter Endpoints**
- `POST /order-item/{orderItemId}/serve`
- `POST /api/orders/{orderId}/change-status`

### **Cashier Endpoints**
- `POST /api/orders/{orderId}/change-status`

---

## 📝 **Postman Collection Setup**

### **Environment Variables**

Create a Postman environment with:
- `baseUrl`: `http://localhost:8080`
- `accessToken`: (set after login)
- `refreshToken`: (set after login)
- `userId`: (set after registration)
- `orderId`: (set after creating order)
- `menuId`: (set after creating menu)
- `orderItemId`: (set after adding item to order)

### **Pre-request Scripts**

For authenticated endpoints, add:
```javascript
pm.request.headers.add({
    key: 'Authorization',
    value: 'Bearer ' + pm.environment.get('accessToken')
});
```

### **Tests Scripts**

After login, save token:
```javascript
if (pm.response.code === 200) {
    const jsonData = pm.response.json();
    pm.environment.set("accessToken", jsonData.accessToken);
    pm.environment.set("refreshToken", jsonData.refreshToken);
}
```

---

## ✅ **Testing Checklist**

- [ ] Backend running on port 8080
- [ ] User registered successfully
- [ ] Login successful, token saved
- [ ] Menu items retrieved
- [ ] Order created successfully
- [ ] Items added to order
- [ ] Order confirmed
- [ ] WebSocket connected
- [ ] WebSocket subscriptions active
- [ ] Notifications received on order confirm
- [ ] Payment processed
- [ ] All role-based endpoints tested (if applicable)

---

## 🚀 **Next Steps**

1. **Test with multiple users** - Open multiple Postman instances
2. **Test concurrent orders** - Create orders from different users simultaneously
3. **Test WebSocket with multiple clients** - Open multiple browser tabs
4. **Test error scenarios** - Invalid data, expired tokens, etc.
5. **Performance testing** - Load test with multiple requests

---

**Happy Testing! 🎉**


