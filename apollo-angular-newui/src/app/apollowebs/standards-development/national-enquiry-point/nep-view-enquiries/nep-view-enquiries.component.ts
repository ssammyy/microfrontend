import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {NepPointService} from "../../../../core/store/data/std/nep-point.service";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {
  InfoAvailableYes,
  ISCheckRequirements,
  NepEnquiries, NepInfoCheckDto,
  NwaRequestList
} from "../../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import swal from "sweetalert2";

declare const $: any;

@Component({
  selector: 'app-nep-view-enquiries',
  templateUrl: './nep-view-enquiries.component.html',
  styleUrls: ['./nep-view-enquiries.component.css']
})
export class NepViewEnquiriesComponent implements OnInit {
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  nepEnquiries: NepEnquiries[]=[];
  public actionRequests: NepEnquiries | undefined;
  blob: Blob;
  public uploadedFiles:  FileList;
  loadingText: string;
  public nepInfoFormGroup!: FormGroup;
  selectedOption = '';


  constructor(
      private formBuilder: FormBuilder,
      private route: ActivatedRoute,
      private router: Router,
      private SpinnerService: NgxSpinnerService,
      private notificationService: NepPointService,
      private notifyService : NotificationService,
  ) { }

  ngOnInit(): void {
    this.nepInfoFormGroup = this.formBuilder.group({
      comments: [],
      accentTo: [],
      enquiryId: [],
      feedbackSent: [],
      requesterEmail: [],
      requesterComment:[],
      requesterSubject:[],
      emailAddress:[]

    });
    this.getNepRequests()
  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)

  }
  showToasterWarning(title:string,message:string){
    this.notifyService.showWarning(message, title)

  }

  onSelected(value:string): void {
    this.selectedOption = value;
  }


  get formSendFeedBack(): any {
    return this.nepInfoFormGroup.controls;
  }
  public getNepRequests(): void {
    this.loadingText = "Retrieving Enquiries...";
    this.SpinnerService.show();
    this.notificationService.getNepRequests().subscribe(
        (response: NepEnquiries[]) => {
          this.nepEnquiries = response;
          this.rerender();
          this.SpinnerService.hide();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }

  public onSubmit(): void{
    this.loadingText = "Saving Response ...."
    this.SpinnerService.show();
    this.notificationService.submitInfoResponse(this.nepInfoFormGroup.value).subscribe(
        (response: NepInfoCheckDto) => {
          console.log(response);
          swal.fire({
            title: 'Confirmed.',
            buttonsStyling: false,
            customClass: {
              confirmButton: 'btn btn-success form-wizard-next-btn ',
            },
            icon: 'success'
          });
          this.SpinnerService.hide();
          this.getNepRequests();
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
          this.SpinnerService.hide();
        }
    );
    this.hideModalEditedDraft()
  }

  public onOpenModal(nepEnquiry: NepEnquiries,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='viewRequests'){
      this.actionRequests=nepEnquiry;
      button.setAttribute('data-target','#viewRequests');

      this.nepInfoFormGroup.patchValue(
          {
            enquiryId: this.actionRequests.id,
            requesterEmail: this.actionRequests.requesterEmail,
            requesterSubject: this.actionRequests.requesterSubject
          }
      );

    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  @ViewChild('closeModalRequirements') private closeModalRequirements: ElementRef | undefined;

  public hideModalRequirements() {
    this.closeModalRequirements?.nativeElement.click();
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

  viewUpload(pdfId: number, fileName: string, applicationType: string): void {
    this.SpinnerService.show();
    this.notificationService.viewUpload(pdfId).subscribe(
        (dataPdf: any) => {
          this.SpinnerService.hide();
          this.blob = new Blob([dataPdf], {type: applicationType});
          let downloadURL = window.URL.createObjectURL(this.blob);
          const link = document.createElement('a');
          link.href = downloadURL;
          link.download = fileName;
          link.click();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

  @ViewChild('closeModalEditedDraft') private closeModalEditedDraft: ElementRef | undefined;

  public hideModalEditedDraft() {
    this.closeModalEditedDraft?.nativeElement.click();
  }

}
