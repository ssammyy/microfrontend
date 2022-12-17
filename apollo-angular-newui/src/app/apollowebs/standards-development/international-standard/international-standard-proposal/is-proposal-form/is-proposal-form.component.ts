import {Component, OnInit, ViewChild} from '@angular/core';
import {StdIntStandardService} from "../../../../../core/store/data/std/std-int-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../../core/store/data/std/notification.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {ISAdoptionProposal, UsersEntity} from "../../../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";
import {Router} from "@angular/router";
import {Store} from "@ngrx/store";
import {selectUserInfo} from "../../../../../core/store";
import {ReplaySubject, Subject} from "rxjs";
import {MatSelect} from "@angular/material/select";
import {take, takeUntil} from "rxjs/operators";

declare const $: any;

@Component({
  selector: 'app-is-proposal-form',
  templateUrl: './is-proposal-form.component.html',
  styleUrls: ['./is-proposal-form.component.css']
})
export class IsProposalFormComponent implements OnInit {
    fullname = '';
  public uploadedFiles: FileList;
  public isProposalFormGroup!: FormGroup;
  title = 'toaster-not';
    proposal_doc_name: string;
    public stakeholdersLists : UsersEntity[]=[] ;
    public websiteCtrl: FormControl = new FormControl();
    public websiteFilterCtrl: FormControl = new FormControl();
    public filteredWebsites: ReplaySubject<any> = new ReplaySubject(1);
    @ViewChild('singleSelect') singleSelect: MatSelect;
    protected _onDestroy = new Subject();
  constructor(
      private store$: Store<any>,
      private formBuilder: FormBuilder,
      private stdIntStandardService : StdIntStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private router: Router,
  ) { }

  ngOnInit(): void {
      this.findStandardStakeholders();
      this.proposal_doc_name='International Standard Adoption Proposal'
    this.isProposalFormGroup = this.formBuilder.group({
      proposal_doc_name: [],
        uploadedBy: [],
        tcSecName : ['', Validators.required],
        circulationDate : ['', Validators.required],
        closingDate : ['', Validators.required],
        title : ['', Validators.required],
        scope : ['', Validators.required],
        iStandardNumber:[],
        stakeholdersList:[],
        addStakeholdersList:[]
        // adoptionAcceptableAsPresented : ['', Validators.required],
        // reasonsForNotAcceptance : ['', Validators.required],
        // recommendations : ['', Validators.required],
        // nameOfRespondent : ['', Validators.required],
        // positionOfRespondent : ['', Validators.required],
        // nameOfOrganization : ['', Validators.required],
        // dateOfApplication : ['', Validators.required],
    });

      this.store$.select(selectUserInfo).pipe().subscribe((u) => {
          return this.fullname = u.fullName;
      });

      this.websiteCtrl.setValue(this.stakeholdersLists[1]);
      this.filteredWebsites.next(this.stakeholdersLists.slice());

      this.websiteFilterCtrl.valueChanges
          .pipe(takeUntil(this._onDestroy))
          .subscribe(() => {
              this.filterBanks();
          });

  }

    ngAfterViewInit() {
        this.setInitialValue();
    }

    ngOnDestroy() {
        this._onDestroy.next();
        this._onDestroy.complete();
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

    protected setInitialValue() {
        this.filteredWebsites
            .pipe(take(1), takeUntil(this._onDestroy))
            .subscribe(() => {
                this.singleSelect.compareWith = (a: UsersEntity, b: UsersEntity) => a && b && a.id === b.id;
            });
    }

  get formISProposal(): any{
    return this.isProposalFormGroup.controls;
  }
  uploadProposal(): void {
    this.SpinnerService.show();
    console.log(this.isProposalFormGroup.value);
    const valueString=this.isProposalFormGroup.get("addStakeholdersList").value.split(",")
    this.stdIntStandardService.prepareAdoptionProposal(this.isProposalFormGroup.value,valueString).subscribe(
        (response) => {
          console.log(response);
          this.SpinnerService.hide();
            this.showToasterSuccess(response.httpStatus, `Proposal Uploaded`);
            swal.fire({
                title: 'Proposal has been Prepared.',
                buttonsStyling: false,
                customClass: {
                    confirmButton: 'btn btn-success ',
                },
                icon: 'success'
            });
          //this.onClickUploadISDocument(response.body.savedRowID)
          this.isProposalFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
            this.showToasterError('Error', `Error Preparing Proposal`);
          alert(error.message);
        }
    );
  }


    onClickUploadISDocument(isProposalID: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }

            this.SpinnerService.show();
            this.stdIntStandardService.uploadFileDetails(isProposalID, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFiles = null;
                    console.log(data);
                    swal.fire({
                        title: 'International Standard Proposal Document Uploaded.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success ',
                        },
                        icon: 'success'
                    });
                    this.router.navigate(['/isProposalForm']);
                },
            );
        }

    }

    showNotification(from: any, align: any) {
        const type = ['', 'info', 'success', 'warning', 'danger', 'rose', 'primary'];

        const color = Math.floor((Math.random() * 6) + 1);

        $.notify({
            icon: 'notifications',
            message: 'Welcome to <b>Material Dashboard</b> - a beautiful dashboard for every web developer.'
        }, {
            type: type[color],
            timer: 3000,
            placement: {
                from: from,
                align: align
            },
            template: '<div data-notify="container" class="col-xs-11 col-sm-3 alert alert-{0} alert-with-icon" role="alert">' +
                '<button mat-raised-button type="button" aria-hidden="true" class="close" data-notify="dismiss">  <i class="material-icons">close</i></button>' +
                '<i class="material-icons" data-notify="icon">notifications</i> ' +
                '<span data-notify="title"></span> ' +
                '<span data-notify="message">Ensure all required fields and items have been filled</span>' +
                '<div class="progress" data-notify="progressbar">' +
                '<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>' +
                '</div>' +
                '<a href="{3}" target="{4}" data-notify="url"></a>' +
                '</div>'
        });
    }

    // private b64Blob(strings: string[], type: any) {
    //     return undefined;
    // }
    //
    // private blobToFile(blob: Blob, test: string) {
    //
    // }

    public findStandardStakeholders(): void {
        this.SpinnerService.show();
        this.stdIntStandardService.findStandardStakeholders().subscribe(
            (response: UsersEntity[]) => {
                this.SpinnerService.hide();
                this.stakeholdersLists = response;
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
    }
    protected filterBanks() {
        if (!this.stakeholdersLists) {
            return;
        }

        let search = this.websiteFilterCtrl.value;
        if (!search) {
            this.filteredWebsites.next(this.stakeholdersLists.slice());
            return;
        } else {
            search = search.toLowerCase();
        }

        this.filteredWebsites.next(
            this.stakeholdersLists.filter(bank => bank.email.toLowerCase().indexOf(search) > -1)
        );
    }
}
