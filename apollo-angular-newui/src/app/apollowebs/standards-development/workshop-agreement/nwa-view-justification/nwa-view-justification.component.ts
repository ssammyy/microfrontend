import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {
  InternationalStandardsComments,
  NWAJustification,
  NwaRequestList
} from "../../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup} from "@angular/forms";
import {StdNwaService} from "../../../../core/store/data/std/std-nwa.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";

@Component({
  selector: 'app-nwa-view-justification',
  templateUrl: './nwa-view-justification.component.html',
  styleUrls: ['./nwa-view-justification.component.css']
})
export class NwaViewJustificationComponent implements OnInit {
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  public approveReviewJustificationFormGroup!: FormGroup;
  nwaRequestLists: NwaRequestList[]=[];
  nwaJustifications: NWAJustification[]=[];
  public actionRequests: NwaRequestList | undefined;
  decisionText: "";
  loadingText: string;
  @ViewChild('accentTo') accentTo!: ElementRef;
  selectedDecision = '';
  onSelected():void {
    this.selectedDecision = this.accentTo.nativeElement.value;
  }

  constructor(
      private store$: Store<any>,
      private router: Router,
      private formBuilder: FormBuilder,
      private stdNwaService: StdNwaService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getWorkshopJustification();
    this.approveReviewJustificationFormGroup = this.formBuilder.group({
      comments: [],
      accentTo: [],
      approvalID:[],
      requestId:[]

    });
  }

  get approveReviewJustificationForm(): any {
    return this.approveReviewJustificationFormGroup.controls;
  }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)

  }

  public getWorkshopJustification(): void {
    this.loadingText = "Retrieving Requests...";
    this.SpinnerService.show();
    this.stdNwaService.getWorkshopJustification().subscribe(
        (response: NwaRequestList[]) => {
          this.nwaRequestLists = response;
          this.rerender();
          this.SpinnerService.hide();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }

  getJustification(id: number){
    this.loadingText = "Loading ...."
    this.SpinnerService.show();
    this.stdNwaService.getJustification(id).subscribe(
        (response: NWAJustification[]) => {
          this.nwaJustifications = response;
          this.SpinnerService.hide();
          //console.log(this.nwaJustifications)
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );


  }

  rerender(): void {
    this.dtElements.forEach((dtElement: DataTableDirective) => {
      if (dtElement.dtInstance)
        dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
          dtInstance.destroy();
        });
    });
    setTimeout(() => {
      this.dtTrigger.next();
    });
  }

  public onOpenModal(nwaRequestList: NwaRequestList,mode:string,requestId: number): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    this.getJustification(requestId)
    if (mode==='reviewJustification'){
      this.actionRequests=nwaRequestList;
      button.setAttribute('data-target','#reviewJustification');
      this.approveReviewJustificationFormGroup.patchValue(
          {
            requestId: this.actionRequests.id,
          }
      );
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  @ViewChild('closeModalUploadJustification') private closeModalUploadJustification: ElementRef | undefined;

  public hideModalUploadDraft() {
    this.closeModalUploadJustification?.nativeElement.click();
  }

  approveJustification(): void {
    this.loadingText = "Approving Justification...";
    this.SpinnerService.show();
    this.stdNwaService.decisionOnJustification(this.approveReviewJustificationFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.getWorkshopJustification();
          this.SpinnerService.hide();
          this.rerender();
          if(this.selectedDecision=="Yes"){
            this.showToasterSuccess('Success', `Justification Was approved`);
          }else if(this.selectedDecision=="No"){
            this.showToasterError('Success', `Justification Was Rejected`);
          }
          this.approveReviewJustificationFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Try Again`);
          console.log(error.message);
        }
    );
    this.hideModelCloseModalReviewJustification();
  }

  @ViewChild('closeModalReviewJustification') private closeModalReviewJustification: ElementRef | undefined;

  public hideModelCloseModalReviewJustification() {
    this.closeModalReviewJustification?.nativeElement.click();
  }


}
