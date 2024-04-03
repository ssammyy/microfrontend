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
  createSale = `${this.baseUrl}/inventory/sale`;
  // Add more order APIs as needed

  // User APIs
  getUsers = `${this.baseUrl}/users`;
  createUser = `${this.baseUrl}/users`;
  // Add more user APIs as needed

  // Other APIs
  // ...

  constructor() { }

  setToken(token: string) {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isLoggedIn(): boolean {
    return this.getToken() !== null;
  }

  logout() {
    localStorage.removeItem('token');
  }
}
