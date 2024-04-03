import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'http://localhost:7111/api/v1';

  // Auth APIs
  authenticate = `${this.baseUrl}/auth/authenticate`;
  // Add more auth APIs as needed

  // Inventory APIs
  getItems = `${this.baseUrl}/inventory/get-items`;
  getAllItems = `${this.baseUrl}/inventory/get-all-items`
  createProduct = `${this.baseUrl}/inventory/products`;
  // Add more inventory APIs as needed

  // Order APIs
  getOrders = `${this.baseUrl}/orders`;
  createOrder = `${this.baseUrl}/orders`;
  // Add more order APIs as needed

  // User APIs
  getUsers = `${this.baseUrl}/users`;
  createUser = `${this.baseUrl}/users`;
  // Add more user APIs as needed

  // Other APIs
  // ...

  constructor() { }
}
