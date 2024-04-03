import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {PageEvent} from "@angular/material/paginator";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpClient} from "@angular/common/http";
import {ApiService} from "../utilities/ApiService";
import {Router} from "@angular/router";
import {Items} from "../DataClasses/Inventory";
import {last} from "rxjs";

@Component({
  selector: 'app-inventory',
  templateUrl: './inventory.component.html',
  styleUrls: ['./inventory.component.css']
})
export class InventoryComponent implements OnInit{




  pagedInventoryItems : Items[] = []
  allInventoryItems : Items[] = []
  cachedItems : any[] =[]
  cartPreview : any[] = []
  price : number=0
  subTotalList : any[] = []
  subTotalAmount : number = 1
  @ViewChild('itemQuantity', { static: false }) myInput!: ElementRef<HTMLInputElement>;
  theLastIndex : [] = []
  theLastIndexValue : [] = []
  dropdownOptions: string[] = ['Option 1', 'Option 2', 'Option 3', 'Option 4', 'Option 5', 'Option 6', 'Option 7', 'Option 8', 'Option 9'];

  groupedOptions: string[][] = [];
  filterTerm!: string;
  totalItems: Number = 0;



  constructor(
    private spinner : NgxSpinnerService,
    private http : HttpClient,
    private apiService : ApiService,
    private router : Router
  ) {
    this.groupedOptions = this.groupOptions(this.dropdownOptions, 3);
  }

  groupOptions(options: string[], groupSize: number): string[][] {
    const grouped: string[][] = [];

    for (let i = 0; i < options.length; i += groupSize) {
      grouped.push(options.slice(i, i + groupSize));
    }

    return grouped;
  }


  /**
   * PAGINATION
   */


  nextPage(event: PageEvent) {
    this.getItems(String(event.pageIndex), String(event.pageSize))

  }

  /**
   * API TO FETCH INVENTORY ITEMS
   */
  getItems(pageNumber: string, pageSize: string): any {
    const payload = {
      pageNumber: pageNumber,
      pageSize: pageSize
    };

    this.spinner.show();
    console.log("payload  "+ payload)

    this.http.post(this.apiService.getItems, payload).subscribe(
      (response: any) => {
        this.pagedInventoryItems = response['content'];
        this.cachedItems = response['content']
        console.log("this is the content "+ response['content'])
      },
      error => {
        console.log("An error occurred: " + error);
      },
      () => {
        this.spinner.hide();
      }
    );
  }

  /***
   * SEARCH FILTER
   */
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

  /**
   * CALCULATE SUB TOTALS
   */
  updatePrice(sp : Items, quan: number){
    this.price = sp.sellingPrice * quan;
    sp.subTotal = this.price
    this.subTotalList.splice(-1,1)
    this.subTotalList.push(this.price)
    // this.theLastIndex.push(this.subTotalList)
    this.subTotalAmount = this.subTotalList.reduce((accumulator, currentValue) => accumulator + currentValue, 0);
  }

  ngOnInit() {
    this.getItems("0", "10")
    this.http.get<any>(this.apiService.getAllItems).subscribe(data => {
      this.allInventoryItems = data.sort();
      this.totalItems= this.allInventoryItems.length
      for(const item of this.allInventoryItems ){

      }



    })

  }



  editItem(item: Items) {

  }

  // addToCart(checked: boolean, item: Items) {
  //   if (checked) {
  //     item.subTotal= item.sellingPrice
  //     item.quantityOnCart = 1
  //     // this.subTotalList.push(item.sellingPrice)
  //     // this.subTotalAmount = this.subTotalList.reduce((accumulator, currentValue) => accumulator + currentValue, 0);
  //
  //     this.cartPreview.push({ ...item, amount: item.sellingPrice });
  //   } else {
  //     const index = this.cartPreview.indexOf(item);
  //     if (index > -1) {
  //       this.cartPreview.splice(index, 1); // Remove the item from the cartPreview array
  //     }
  //   }
  // }

  isItemInCart(item: Items): boolean{
    return this.cartPreview.includes(item)
  }









  addToCart(item: any, checked : boolean) {
    const cartItem = { ...item, quantity: 1, price: item.price, amount: item.price };


    if (checked) {
        this.cartPreview.push(cartItem)
      } else {
        const index = this.cartPreview.indexOf(cartItem);
        if (index > -1) {
          this.cartPreview.splice(index, 1); // Remove the item from the cartPreview array
        }
      }

    // this.cartPreview.push({ ...item, amount: item.price });
  }

  updateQuantity(item: any, newQuantity: number) {
    item.quantity = newQuantity;
    item.amount = item.price * newQuantity;
  }

  updatePriceVz(item: any, newPrice: number) {
    item.price = newPrice;
    item.amount = item.quantity * newPrice;
  }

  removeFromCart(index: number) {
    this.cartPreview.splice(index, 1);
  }

  getTotalCost() {
    return this.cartPreview.reduce((total, item) => total + item.amount, 0);
  }



  protected readonly Number = Number;
}
