import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Items} from "../DTOs/Inventory";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Router} from "@angular/router";
import {ApiService} from "../Utils/ApiService";
import {PageEvent} from "@angular/material/paginator";
import {MessageService} from "primeng/api";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";


@Component({
  selector: 'app-inventory',
  templateUrl: './inventory.component.html',
  styleUrls: ['./inventory.component.css'],
  providers: [MessageService]


})
export class InventoryComponent implements OnInit{




  pagedInventoryItems : Items[] = []
  allInventoryItems : Items[] = []
  cachedItems : any[] =[]
  cartPreview : any[] = []
  debtAmount : number= 0
  price : number=0
  saleCode:any;
  loading = false;
  subTotalList : any[] = []
  selectedPaymentMethod : string=''
  saleCleared = false;
  saleCredit = false;
  exceeds = false
  custTel : string=''
  custName : string=''
  subTotalAmount : number = 1
  @ViewChild('itemQuantity', { static: false }) myInput!: ElementRef<HTMLInputElement>;
  theLastIndex : [] = []
  theLastIndexValue : [] = []
  dropdownOptions: string[] = ['Option 1', 'Option 2', 'Option 3', 'Option 4', 'Option 5', 'Option 6', 'Option 7', 'Option 8', 'Option 9'];

  groupedOptions: string[][] = [];
  filterTerm!: string;
  totalItems: Number = 0;
  itemQuantity = 1;
  itemCartAmount =0;
  visible = false;
  cartTotalAmount = 0

  ref: DynamicDialogRef | undefined;


  constructor(
    private http : HttpClient,
    private apiService : ApiService,
    private messageService: MessageService,
    private router : Router,
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



  editItem(item: Items) {

  }


  isItemInCart(item: Items): boolean{
    return this.cartPreview.includes(item)
  }









  addToCart(item: any, checked : boolean) {
    console.log("cart preview "+ this.cartPreview)
    item.amount = item.sellingPrice;
    const cartItem = { ...item, quantity: item.quantity, price: item.price, amount: item.price };


    if (checked) {
      this.cartPreview.push(cartItem)

    } else {
      const index = this.cartPreview.indexOf(cartItem);
      if (index > -1) {
        this.cartPreview.splice(index, 1);
      }
    }

    // this.cartPreview.push({ ...item, amount: item.price });
  }

  updateQuantity(item: any, newQuantity: Event) {

    const newQuan = (event as any).target.valueAsNumber;
    console.log("item quantity is "+ item.quantity)
    if (newQuan > item.quantity){
      this.exceeds =true
      console.log("bigger than inventory")

      this.showWarn("Not enough quantity for "+item.itemName )
    }else{
      this.exceeds =false
      this.itemQuantity = newQuan;
      item.amount = this.itemQuantity * item.sellingPrice
      this.itemCartAmount = this.itemQuantity * item.sellingPrice
    }

    // item.amount = item.price * newQuantity;
  }

  updatePriceVz(item: any, newPrice: number) {
    item.price = newPrice;
    item.amount = item.quantity * newPrice;
  }

  removeFromCart(index: number) {
    this.cartPreview.splice(index, 1);
  }

  goToCheckOut(){
    console.log('checkout clicked')
  }

  showWarn(message: string) {
    this.messageService.add({ severity: 'warn', summary: 'Warn', detail: message });
  }


  getTotalCost() {
    if (this.exceeds){
      return 'NAN'
    }
    return this.cartTotalAmount = this.cartPreview.reduce((total, item) => total + item.amount,0);
  }
  showModal(cart: any) {
    this.visible = true;
    this.generateRandomString(6);
    for (let i =0; i< cart.length; i++){
      console.log(cart[i].itemName)
    }

  }
   generateRandomString(length: number): string {
    const characters = '0123456789';
    let result = '';
    const charactersLength = characters.length;
    for (let i = 0; i < length; i++) {
      result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }
    this.saleCode="#NSM"+result;
    return result;
  }
  finishSale(itemList : any[]){
    console.log("customer name "+ this.custName)
    if(!this.saleCredit && !this.saleCleared){
      this.showWarn('select whether order is cleared or on credit')

    }else if(this.selectedPaymentMethod==""){
      this.showWarn('select payment method')
    }else if(this.selectedPaymentMethod=="M-Pesa"){
      if (this.custTel =="" || this.custName==""){
        this.showWarn('key in customer details')
      }else {
        this.processSale(itemList)
      }
    }else{
      this.processSale(itemList)
    }
  }

  processSale(itemList: any[]){
    this.loading= true
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.apiService.getToken()}`);

    const payload = {
      referenceCode: this.saleCode,
      items: itemList,
      isCleared: this.saleCleared,
      isCredit: this.saleCredit,
      customerName: this.custName,
      customerTel: this.custTel,
      creditAmount: 0,
      saleTotal : this.cartTotalAmount
    };

    this.http.post(this.apiService.createSale, payload, {headers} )    .subscribe(
      (response: any) => {
        this.showSuccess('Sale success')
        setTimeout(()=>{
          this.visible = false
          this.cartPreview.splice(0, this.cartPreview.length)
          this.loading = false
          this.router.navigate(['/inventory'])
        }, 2000)


      },
      error => {
        this.loading = false
        this.showWarn('an error occurred try again later ')

      }
    );
    console.log("this is the payload "+ payload)
  }

  showSuccess(message : string) {
    this.messageService.add({ severity: 'success', summary: 'Success', detail: message });
  }

  updateSaleCleared() {
    this.saleCredit = false;
    this.saleCleared = true;
  }

  updateSaleCredit(){
    this.saleCredit = true;
    this.saleCleared = false;
  }




  protected readonly Number = Number;
  protected readonly isNaN = isNaN;
}
