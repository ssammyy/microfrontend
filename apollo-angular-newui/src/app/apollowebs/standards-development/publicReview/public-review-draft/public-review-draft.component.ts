import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {
    CommentMadeRetrieved,
    Preliminary_Draft, PublicReviewDraftWithName, StandardDocuments
} from "../../../../core/store/data/std/commitee-model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {CommitteeService} from "../../../../core/store/data/std/committee.service";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {PublicReviewService} from "../../../../core/store/data/std/publicReview.service";
import {formatDate} from "@angular/common";
import Swal from "sweetalert2";
import swal from "sweetalert2";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {
    DataHolderB,
    Department
} from "../../../../core/store/data/std/request_std.model";
import {IDropdownSettings} from "ng-multiselect-dropdown";
import {NepPointService} from "../../../../core/store/data/std/nep-point.service";
declare const $: any;
@Component({
    selector: 'app-public-review-draft',
    templateUrl: './public-review-draft.component.html',
    styleUrls: ['./public-review-draft.component.css']
})
export class PublicReviewDraftComponent implements OnInit {

    fullname = '';
    title = 'toaster-not';
    submitted = false;

    public editedPublicReview_draftFormGroup!: FormGroup;

    public publicReviewDrafts !: PublicReviewDraftWithName[];
    public publicReviewDraftsB !: PublicReviewDraftWithName | undefined;
    standardDocuments !: StandardDocuments[];

    dateFormat = "yyyy-MM-dd";
    language = "en";
    commentMadeRetrievedS !: CommentMadeRetrieved[];
    commentMadeRetrievedB !: CommentMadeRetrieved | undefined;

    dtOptions: DataTables.Settings = {};
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
    dtTrigger3: Subject<any> = new Subject<any>();

    public departments !: Department[];
    dropdownList: any[] = [];
    selectedItems?: Department;
    public dropdownSettings: IDropdownSettings = {};

    blob: Blob;
    public uploadedFiles: FileList;

    loadingText: string;
    public preparePreliminaryDraftFormGroup!: FormGroup;
    addressOfAgency: string;
    telephoneOfAgency: string;
    faxOfAgency: string;
    emailOfAgency: string;
    websiteOfAgency: string;
    textAvailableFrom: string;
    notifyingMember: string;
    agencyResponsible: string;

    constructor(private formBuilder: FormBuilder,
                private committeeService: CommitteeService,
                private store$: Store<any>,
                private router: Router,
                private standardDevelopmentService: StandardDevelopmentService,
                private publicReviewService: PublicReviewService,
                private notificationService: NepPointService,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService) {
    }

    ngOnInit(): void {
        this.getAllPrdS();
        this.getAllDepartments()
        this.editedPublicReview_draftFormGroup = this.formBuilder.group({
            prdName: ['', Validators.required],
            ksNumber:['', Validators.required]
        });
        this.dropdownSettings = {
            singleSelection: false,
            idField: 'id',
            textField: 'name',
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            allowSearchFilter: true
        };

        this.preparePreliminaryDraftFormGroup = this.formBuilder.group({
            notifyingMember : ['', Validators.required],
            agencyResponsible : ['', Validators.required],
            addressOfAgency : ['', Validators.required],
            telephoneOfAgency : ['', Validators.required],
            faxOfAgency : ['', Validators.required],
            emailOfAgency : ['', Validators.required],
            websiteOfAgency : ['', Validators.required],
            notifiedUnderArticle : ['', Validators.required],
            productsCovered : ['', Validators.required],
            descriptionOfNotifiedDoc : ['', Validators.required],
            descriptionOfContent : ['', Validators.required],
            objectiveAndRationale : ['', Validators.required],
            relevantDocuments : ['', Validators.required],
            proposedDateOfAdoption : ['', Validators.required],
            proposedDateOfEntryIntoForce : [],
            textAvailableFrom : ['', Validators.required],
            pid:null,
            cd_Id:null,
            prd_name:null,
            ks_NUMBER:null,
            organization:null,
            prd_by: null,
            status: null,
            created_on: null,
            number_OF_COMMENTS: null,
            var_FIELD_1: null

        });
        this.notifyingMember="Republic of Kenya"
        this.agencyResponsible="Kenya Bureau of Standards - KEBS"
        this.addressOfAgency="P.O. Box: 54974-00200, Nairobi, Kenya"
        this.telephoneOfAgency="+ (254) 020 605490, 605506/6948258"
        this.faxOfAgency="+ (254) 020 609660/609665"
        this.emailOfAgency="info@kebs.org"
        this.websiteOfAgency="Website: http://www.kebs.org"
        this.textAvailableFrom="Kenya Bureau of Standards\n WTO/TBT National Enquiry Point\n P.O. Box: 54974-00200, Nairobi, Kenya\n Telephone: + (254) 020 605490, 605506/6948258\n Fax: + (254) 020 609660/609665\n E-mail: info@kebs.org; Website: http://www.kebs.org\n"
        this.preparePreliminaryDraftFormGroup.patchValue(
            {
                addressOfAgency: this.addressOfAgency,
                telephoneOfAgency: this.telephoneOfAgency,
                faxOfAgency: this.faxOfAgency,
                emailOfAgency: this.emailOfAgency,
                websiteOfAgency: this.websiteOfAgency,
                textAvailableFrom: this.textAvailableFrom,
                notifyingMember:this.notifyingMember,
                agencyResponsible:this.agencyResponsible
            }
        );

    }

    showToasterWarning(title:string,message:string){
        this.notifyService.showWarning(message, title)

    }
    get formPreparePD(): any {
        return this.preparePreliminaryDraftFormGroup.controls;
    }

    public getAllPrdS(): void {
        this.publicReviewService.getAllPublicReviewDrafts().subscribe(
            (response: PublicReviewDraftWithName[]) => {
                console.log(response);

                this.publicReviewDrafts = response;
                this.rerender()
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );

    }

    public getAllPrddDocs(pdID: number) {
        this.publicReviewService.getAllDocumentsOnPrd(pdID).subscribe(
            (response: StandardDocuments[]) => {

                this.standardDocuments = response;
                this.rerender()

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    public getAllComments(prdID: number) {
        this.publicReviewService.getAllCommentsOnPrd(prdID).subscribe(
            (response: CommentMadeRetrieved[]) => {

                this.commentMadeRetrievedS = response;
                this.rerender()

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    public getAllDepartments(): void {
        this.standardDevelopmentService.getDepartmentsb().subscribe(
            (response: Department[]) => {
                this.departments = response;
                this.dropdownList = response;

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );


    }

    public sendToHeadOfAffairs(publicReviewDrafts: PublicReviewDraftWithName) {
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-danger'
            },
            buttonsStyling: false
        });

        swalWithBootstrapButtons.fire({
            title: 'Are you sure your want to Sent To Head Of Trade Affairs?',
            text: 'You won\'t be able to reverse this!',
            icon: 'success',
            showCancelButton: true,
            confirmButtonText: 'YES!',
            cancelButtonText: 'NO!',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                this.SpinnerService.show();
                this.publicReviewService.sendToOrganisation(publicReviewDrafts).subscribe(
                    (response) => {
                        this.SpinnerService.hide();
                        swalWithBootstrapButtons.fire(
                            'Success!',
                            'Successfully Sent!',
                            'success'
                        );
                        this.SpinnerService.hide();
                        this.getAllPrdS();
                    },
                );
            } else if (
                /* Read more about handling dismissals below */
                result.dismiss === swal.DismissReason.cancel
            ) {
                swalWithBootstrapButtons.fire(
                    'Cancelled',
                    'You have cancelled this operation',
                    'error'
                );
            }
        });

    }
    public postToWebsite(publicReviewDrafts: PublicReviewDraftWithName) {
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-danger'
            },
            buttonsStyling: false
        });

        swalWithBootstrapButtons.fire({
            title: 'Are you sure your want to Post On The KEBS Website?',
            text: 'You won\'t be able to reverse this!',
            icon: 'success',
            showCancelButton: true,
            confirmButtonText: 'YES!',
            cancelButtonText: 'NO!',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                this.SpinnerService.show();
                this.publicReviewService.postToWebsite(publicReviewDrafts).subscribe(
                    (response) => {
                        this.SpinnerService.hide();
                        swalWithBootstrapButtons.fire(
                            'Success!',
                            'Successfully Posted!',
                            'success'
                        );
                        this.SpinnerService.hide();
                        this.getAllPrdS();
                    },
                );
            } else if (
                /* Read more about handling dismissals below */
                result.dismiss === swal.DismissReason.cancel
            ) {
                swalWithBootstrapButtons.fire(
                    'Cancelled',
                    'You have cancelled this operation',
                    'error'
                );
            }
        });

    }

    public sendToDepartment(secTask: DataHolderB, id: number): void {

        if (secTask.departmentData != null) {
            //console.log(JSON.stringify(secTask.liaisonOrganisationData.name));
            secTask.department = JSON.stringify(secTask.departmentData);


            const swalWithBootstrapButtons = Swal.mixin({
                customClass: {
                    confirmButton: 'btn btn-success',
                    cancelButton: 'btn btn-danger'
                },
                buttonsStyling: false
            });

            swalWithBootstrapButtons.fire({
                title: 'Are you sure your want to Sent To Interested Departments/Organisations?',
                text: 'You won\'t be able to reverse this!',
                icon: 'success',
                showCancelButton: true,
                confirmButtonText: 'Yes!',
                cancelButtonText: 'No!',
                reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    this.SpinnerService.show();
                    this.publicReviewService.sendToDepartments(secTask, String(id)).subscribe(
                        (response) => {
                            this.SpinnerService.hide();
                            swalWithBootstrapButtons.fire(
                                'Success!',
                                'Successfully Sent!',
                                'success'
                            );
                            this.SpinnerService.hide();
                            this.getAllPrdS();
                            this.hideModelB()
                        },
                    );
                } else if (
                    /* Read more about handling dismissals below */
                    result.dismiss === swal.DismissReason.cancel
                ) {
                    swalWithBootstrapButtons.fire(
                        'Cancelled',
                        'You have cancelled this operation',
                        'error'
                    );
                }
            });
        } else {
            this.showToasterError("Error", `Please Select An Organisation.`);

        }

    }

    uploadPublicReviewDraft(): void {
        if (this.uploadedFiles != null) {
            this.SpinnerService.show();
            this.publicReviewService.prepareEditedPublicReviewDraft(this.editedPublicReview_draftFormGroup.value, String(this.publicReviewDraftsB.id)).subscribe(
                (response) => {
                    console.log(response)
                    this.SpinnerService.hide();
                    this.showToasterSuccess(response.httpStatus, `Successfully Submitted Edited Public Review Draft`);
                    this.uploadEditedPrDDoc(response.body.savedRowID)
                    this.editedPublicReview_draftFormGroup.reset();
                },
                (error: HttpErrorResponse) => {
                    this.SpinnerService.hide();
                    console.log(error.message);
                }
            );
        } else {
            this.showToasterError("Error", `Please Upload all the documents.`);
        }
    }

    public uploadEditedPrDDoc(prdId: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.publicReviewService.uploadEditedPRDDocument(prdId, formData, "PRD Document", "EDITED PRD Document").subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFiles = null;
                    console.log(data);
                    this.hideModelC()
                    swal.fire({
                        title: 'Edited Public Review Draft Document Uploaded Successfully.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                    this.getAllPrdS();

                },
            );
        }
    }

    public approveDraft(publicReviewDrafts: PublicReviewDraftWithName) {
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-danger'
            },
            buttonsStyling: false
        });

        swalWithBootstrapButtons.fire({
            title: 'Are you sure your want to Approve This Public Review Draft?',
            text: 'You won\'t be able to reverse this!',
            icon: 'success',
            showCancelButton: true,
            confirmButtonText: 'Approve!',
            cancelButtonText: 'Reject!',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                this.SpinnerService.show();
                this.publicReviewService.approvePublicReviewDraft(publicReviewDrafts).subscribe(
                    (response) => {
                        this.SpinnerService.hide();
                        swalWithBootstrapButtons.fire(
                            'Success!',
                            'Successfully Approved!',
                            'success'
                        );
                        this.SpinnerService.hide();
                        this.getAllPrdS();
                    },
                );
            } else if (
                /* Read more about handling dismissals below */
                result.dismiss === swal.DismissReason.cancel
            ) {
                swalWithBootstrapButtons.fire(
                    'Cancelled',
                    'You have cancelled this operation',
                    'error'
                );
            }
        });

    }

    public onOpenModal(publicReviewDrafts: PublicReviewDraftWithName, mode: string): void {

        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        if (mode === 'prepareNotification') {
            this.publicReviewDraftsB = publicReviewDrafts
            button.setAttribute('data-target', '#prepareNotification');
            this.preparePreliminaryDraftFormGroup.patchValue(
                {
                    addressOfAgency: this.addressOfAgency,
                    telephoneOfAgency: this.telephoneOfAgency,
                    faxOfAgency: this.faxOfAgency,
                    emailOfAgency: this.emailOfAgency,
                    websiteOfAgency: this.websiteOfAgency,
                    textAvailableFrom: this.textAvailableFrom,
                    notifyingMember:this.notifyingMember,
                    agencyResponsible:this.agencyResponsible,
                    pid:this.publicReviewDraftsB.id,
                    cd_Id:this.publicReviewDraftsB.cd_Id,
                    prd_name:this.publicReviewDraftsB.prd_name,
                    ks_NUMBER:this.publicReviewDraftsB.ks_NUMBER,
                    organization:this.publicReviewDraftsB.organization,
                    prd_by:this.publicReviewDraftsB.prd_by,
                    status:this.publicReviewDraftsB.status,
                    created_on:this.publicReviewDraftsB.created_on,
                    number_OF_COMMENTS:this.publicReviewDraftsB.number_OF_COMMENTS,
                    var_FIELD_1:this.publicReviewDraftsB.var_FIELD_1
                }
            );
        }
        if (mode === 'comments') {
            console.log(publicReviewDrafts)
            this.publicReviewDraftsB = publicReviewDrafts;
            this.getAllComments(publicReviewDrafts.id)

            button.setAttribute('data-target', '#comments');
        }
        if (mode === 'editPublicReviewDraft') {
            this.publicReviewDraftsB = publicReviewDrafts
            button.setAttribute('data-target', '#editPublicReviewDraft');
        }
        if (mode === 'sendToOrganisations') {
            this.publicReviewDraftsB = publicReviewDrafts

            button.setAttribute('data-target', '#sendToOrganisations');
        }
        if (mode === 'moreDetails') {
            this.publicReviewDraftsB = publicReviewDrafts
            this.getAllPrddDocs(publicReviewDrafts.id)

            button.setAttribute('data-target', '#moreDetails');
        }


        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    public onOpenModalB(commentMadeRetrieved: CommentMadeRetrieved, mode: string): void {

        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        if (mode === 'viewComment') {
            this.commentMadeRetrievedB = commentMadeRetrieved;
            button.setAttribute('data-target', '#viewComment');

        }


        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    display = 'none'; //default Variable
    @ViewChild('closeModal') private closeModal: ElementRef | undefined;
    @ViewChild('closeModalB') private closeModalB: ElementRef | undefined;
    @ViewChild('closeModalC') private closeModalC: ElementRef | undefined;
    @ViewChild('closeModalD') private closeModalD: ElementRef | undefined;
    @ViewChild('closeModalE') private closeModalE: ElementRef | undefined;

    public hideModel() {
        this.closeModal?.nativeElement.click();
    }

    public hideModelB() {
        this.closeModalB?.nativeElement.click();
    }

    public hideModelC() {
        this.closeModalC?.nativeElement.click();
    }

    public hideModelD() {
        this.closeModalD?.nativeElement.click();
    }

    public hideModelE() {
        this.closeModalE?.nativeElement.click();
    }


    viewPdfFile(pdfId: number, fileName: string, applicationType: string, doctype: string): void {
        this.SpinnerService.show();
        this.committeeService.viewDocs(pdfId, doctype).subscribe(
            (dataPdf: any) => {
                this.SpinnerService.hide();
                this.blob = new Blob([dataPdf], {type: applicationType});

                // tslint:disable-next-line:prefer-const
                let downloadURL = window.URL.createObjectURL(this.blob);
                const link = document.createElement('a');
                link.href = downloadURL;
                link.download = fileName;
                link.click();
                // this.pdfUploadsView = dataPdf;
            },
        );
    }

    viewPdfFileById(pdfId: number, fileName: string, applicationType: string, doctype: string): void {
        this.SpinnerService.show();
        this.committeeService.viewDocsById(pdfId).subscribe(
            (dataPdf: any) => {
                this.SpinnerService.hide();
                this.blob = new Blob([dataPdf], {type: applicationType});

                // tslint:disable-next-line:prefer-const
                let downloadURL = window.URL.createObjectURL(this.blob);
                const link = document.createElement('a');
                link.href = downloadURL;
                link.download = fileName;
                link.click();
                // this.pdfUploadsView = dataPdf;
            },
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
            this.dtTrigger1.next();
            this.dtTrigger2.next();
            this.dtTrigger3.next();

        });

    }


    ngOnDestroy(): void {
        // Do not forget to unsubscribe the event
        this.dtTrigger1.unsubscribe();
        this.dtTrigger2.unsubscribe();
        this.dtTrigger3.unsubscribe();

    }

    formatFormDate(date: string) {
        return formatDate(date, this.dateFormat, this.language);
    }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)
    }

    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)

    }

    notificationOfReview(): void {
        this.loadingText = "Saving...";
        this.SpinnerService.show();
        this.notificationService.notificationOfReview(this.preparePreliminaryDraftFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Preliminary Draft  Uploaded`);
                this.onClickSaveUploads(response.body.id)
                this.preparePreliminaryDraftFormGroup.reset();

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Preliminary Draft Was Not Prepared`);
                console.log(error.message);
            }
        );
    }

    onClickSaveUploads(draftId: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.notificationService.uploadDraft(draftId, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFiles = null;
                    console.log(data);
                    swal.fire({
                        title: 'Thank you....',
                        html:'Uploaded',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    }).then(r => this.preparePreliminaryDraftFormGroup.reset());
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
                '<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" ></div>' +
                '</div>' +
                '<a href="{3}" target="{4}" data-notify="url"></a>' +
                '</div>'
        });
    }

}
