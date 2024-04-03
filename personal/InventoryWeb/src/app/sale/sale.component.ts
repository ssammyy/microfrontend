import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ApiService} from "../Utils/ApiService";
import {MessageService} from "primeng/api";
import {Router} from "@angular/router";
import {EditedItem, Items, NewProduct} from "../DTOs/Inventory";
import {PageEvent} from "@angular/material/paginator";

@Component({
  selector: 'app-sale',
  templateUrl: './sale.component.html',
  styleUrls: ['./sale.component.css'],
  providers: [MessageService]

})
export class SaleComponent implements OnInit{
  loading = false
  groupedOptions: string[][] = [];
  pagedInventoryItems : Items[] = []
  allInventoryItems : Items[] = []
  totalItems: Number = 0;
  cachedItems : any[] =[]
  isVisible= false
  filterTerm =''
  editedItem: EditedItem= {
    id: 0,
    itemName: '',
    sellingPrice: 0,
    buyingPrice: 0,
    quantity: 0
  }

  newProduct: NewProduct = {
    itemName: '',
    sellingPrice: 0,
    buyingPrice: 0,
    quantity: 0
  }

  productsToAdd: NewProduct[] = []; // Array to store multiple products



  constructor(
    private http : HttpClient,
    private apiService : ApiService,
    private messageService: MessageService,
    private router : Router,
  ) {
  }


  ngOnInit() {
    this.loading=true
    this.getItems("0", "10")
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.apiService.getToken()}`);

    this.http.get<any>(this.apiService.getAllItems, {headers}).subscribe(data => {
      this.allInventoryItems = data.sort();
      this.totalItems= this.allInventoryItems.length
      this.loading = false
      for(const item of this.allInventoryItems){

      }



    }, error => {
      console.log()
      this.loading =false
      this.showWarn('An error occurred')
    })
  }

  getItems(pageNumber: string, pageSize: string): any {
    const payload = {
      pageNumber: pageNumber,
      pageSize: pageSize
    };

    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.apiService.getToken()}`);
    this.http.post(this.apiService.getItems, payload, {headers}).subscribe(
      (response: any) => {
        this.pagedInventoryItems = response['content'];
        this.cachedItems = response['content']
      },
      error => {
        this.showWarn('Failed to load inventory, reload page')

      },
      () => {
      }
    );
  }

  editItem(item : any){
    this.editedItem = item
    this.isVisible = true
  }
  addMultipleProducts(){

  }


  showWarn(message: string) {
    this.messageService.add({ severity: 'warn', summary: 'Warn', detail: message });
  }

  showSuccess(message : string) {
    this.messageService.add({ severity: 'success', summary: 'Success', detail: message });
  }

  nextPage(event: PageEvent) {
    this.getItems(String(event.pageIndex), String(event.pageSize))
  }

  filterItems() {
    this.pagedInventoryItems = this.allInventoryItems.filter(item =>
      item.itemName.toLowerCase().includes(this.filterTerm.toLowerCase())
    );
    this.totalItems = this.pagedInventoryItems.length
    if (this.filterTerm=="") {
      this.pagedInventoryItems = this.cachedItems
      this.totalItems = this.allInventoryItems.length
    }
  }

  onSubmit(){

  }

  onSubmitNewProduct(){

  }
  cancelEdit(){
    this.isVisible=false
  }

  saveChanges() {

    console.log('Saving changes:', this.editedItem);
    this.isVisible = false;
  }
  addProductRow() {
    this.productsToAdd.push({ itemName: '', quantity: 0, buyingPrice: 0, sellingPrice: 0 });
  }
  removeProductRow(index: number) {
    if (this.productsToAdd.length > 1) {
      this.productsToAdd.splice(index, 1);
    }
  }


}
