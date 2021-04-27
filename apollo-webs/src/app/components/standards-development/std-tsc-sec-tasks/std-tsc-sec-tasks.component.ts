import { Component, OnInit } from '@angular/core';
import {HttpErrorResponse} from "@angular/common/http";
import {StandardDevelopmentService} from "../../../shared/services/standard-development.service";
import {Product, StandardRequest, StandardTasks, Stdtsectask} from "../../../shared/models/standard-development";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-std-tsc-sec-tasks',
  templateUrl: './std-tsc-sec-tasks.component.html',
  styleUrls: ['./std-tsc-sec-tasks.component.css']
})
export class StdTscSecTasksComponent implements OnInit {

  p = 1;
  p2 = 1;
  public secTasks : Stdtsectask[] =[];
  public tscsecRequest !: Stdtsectask | undefined;
  public stdTSecFormGroup!: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private  standardDevelopmentService:StandardDevelopmentService
  ) { }

  ngOnInit(): void {
    this.getTCSECTasks();
    this.stdTSecFormGroup = this.formBuilder.group({
      proposal_title:['', Validators.required],
      nwi_scope:['', Validators.required],
      target_date:['', Validators.required],
      similar_standards:['', Validators.required],
      relevant_documents:['', Validators.required],
      liaison_organisation:['', Validators.required],
      draft_outline:['', Validators.required],
      organization:['', Validators.required],
      circulation_date:['', Validators.required],
      closing_date:['', Validators.required]
      // postalAddress: ['', [Validators.required, Validators.pattern('P.O.BOX [0-9]{5}')]]
    });
  }

  get formStdTSec(): any{
    return this.stdTSecFormGroup.controls;
  }

  public getTCSECTasks(): void{
    this.standardDevelopmentService.getTCSECTasks().subscribe(
      (response: Stdtsectask[]) =>{
        console.log(response);
        this.secTasks=response;
      },
      (error:HttpErrorResponse) =>{
        alert(error.message);
      }
    )
  }

  public onUpload(secTask: Stdtsectask): void{
    this.standardDevelopmentService.uploadNWI(secTask).subscribe(
      (response:Stdtsectask) =>{
        console.log(response);
        this.getTCSECTasks();
      },
      (error:HttpErrorResponse) =>{
        alert(error.message);
      }
    )
  }

  uploadNWI(): void{

    this.standardDevelopmentService.uploadNWI(this.stdTSecFormGroup.value).subscribe(
      (response:Stdtsectask) =>{
        console.log(response);
        this.getTCSECTasks();
      },
      (error:HttpErrorResponse) =>{
        alert(error.message);
      }
    );
  }

  public onOpenModal(secTask: Stdtsectask,mode:string): void {

    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode==='edit'){
      this.tscsecRequest=secTask;
      button.setAttribute('data-target','#updateNWIModal');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

}
