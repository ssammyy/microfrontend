import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {LiaisonOrganization, Stdtsectask} from "../../../../core/store/data/std/request_std.model";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {HttpErrorResponse} from "@angular/common/http";
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import {ListItem} from "ng-multiselect-dropdown/multiselect.model";
import {Subject} from "rxjs";

@Component({
  selector: 'app-std-tsc-sec-tasks-component',
  templateUrl: './std-tsc-sec-tasks-component.component.html',
  styleUrls: ['./std-tsc-sec-tasks-component.component.css']
})
export class StdTscSecTasksComponentComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  p = 1;
  p2 = 1;
  public itemId :string="";
  public filePurposeAnnex: string="FilePurposeAnnex";
  public relevantDocumentsNWI: string="RelevantDocumentsNWI";

  public secTasks: Stdtsectask[] = [];
  public tscsecRequest !: Stdtsectask | undefined;
  public uploadedFiles: FileList;

  //public stdTSecFormGroup!: FormGroup;

  public liaisonOrganizations !: LiaisonOrganization[];

  dropdownList:any[] = [];
  selectedItems?: LiaisonOrganization ;

  //selectedItems = "";

  public dropdownSettings: IDropdownSettings ={};

  constructor(
      private formBuilder: FormBuilder,
      private  standardDevelopmentService: StandardDevelopmentService
  ) {
  }

  ngOnInit(): void {
    this.getTCSECTasks();
    this.getLiasisonOrganization();



    console.log(this.dropdownList);



    this.dropdownSettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'name',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 3,
      allowSearchFilter: true
    };

  }

  public getLiasisonOrganization(): void {
    this.standardDevelopmentService.getLiaisonOrganization().subscribe(
        (response: LiaisonOrganization[]) => {
          this.liaisonOrganizations = response;
          this.dropdownList = response;
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    );
  }

  onItemSelect(item: ListItem) {
    console.log(item);
  }
  onSelectAll(items: any) {
    console.log(items);
  }

  // get formStdTSec(): any {
  //return this.stdTSecFormGroup.controls;
  // }

  public getTCSECTasks(): void {
    this.standardDevelopmentService.getTCSECTasks().subscribe(
        (response: Stdtsectask[]) => {
          console.log(response);
          this.secTasks = response;
          this.dtTrigger.next();

        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    )
  }

  @ViewChild('closeModal') private closeModal: ElementRef | undefined;
  public hideModel() {
    this.closeModal?.nativeElement.click();
  }

  public onUpload(secTask: Stdtsectask): void {

    if (secTask.liaisonOrganisationData != null) {
      //console.log(JSON.stringify(secTask.liaisonOrganisationData.name));
      secTask.liaisonOrganisation = JSON.stringify(secTask.liaisonOrganisationData);
      console.log(secTask);
    }

    this.standardDevelopmentService.uploadNWI(secTask).subscribe(
        (response: Stdtsectask) => {
          console.log(response);
          this.hideModel();
          this.getTCSECTasks();
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    )
  }

  /*uploadNWI(): void {
    this.standardDevelopmentService.uploadNWI(this.stdTSecFormGroup.value).subscribe(
      (response: Stdtsectask) => {
        console.log(response);
        this.getTCSECTasks();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }*/

  public onOpenModal(secTask: Stdtsectask, mode: string): void {

    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'edit') {
      console.log(secTask.taskId)
      this.tscsecRequest = secTask;
      this.itemId = this.tscsecRequest.taskData.requestNumber;
      button.setAttribute('data-target', '#updateNWIModal');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

}
