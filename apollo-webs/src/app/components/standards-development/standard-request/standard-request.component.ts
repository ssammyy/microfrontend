import { Component, OnInit } from '@angular/core';
import {StandardRequestService} from "../../../shared/services/standard-request.service";
import {FormControl, FormGroup, NgForm} from "@angular/forms";
import {Product, StandardRequest} from "./standardrequest";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-standard-request',
  templateUrl: './standard-request.component.html',
  styleUrls: ['./standard-request.component.css']
})
export class StandardRequestComponent implements OnInit {

  public products : Product[] | undefined;

  constructor(private standardRequestService:StandardRequestService) { }



  ngOnInit() {
   this.getStandards();
  }

  public getStandards(): void{
    this.standardRequestService.getStandards().subscribe(
      (response: Product[])=> {
        this.products = response;
      },
      (error: HttpErrorResponse)=>{
        alert(error.message);
      }
    );
  }

  public saveStandard(savestandard: NgForm): void{

    this.standardRequestService.addStandardRequest(savestandard.value).subscribe(
      (response:StandardRequest) =>{
        console.log(response);
        savestandard.resetForm();
      },
      (error:HttpErrorResponse) =>{
        alert(error.message);
        savestandard.resetForm();
      }
    )
  }


}
