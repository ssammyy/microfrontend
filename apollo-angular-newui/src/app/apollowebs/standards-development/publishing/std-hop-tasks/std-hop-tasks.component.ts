import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DraftPublishing, StdTCDecision} from "../../../../core/store/data/std/request_std.model";
import {PublishingService} from "../../../../core/store/data/std/publishing.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {CommitteeService} from "../../../../core/store/data/std/committee.service";
import {
    ComStdCommitteeRemarks, ComStdRemarks,
    InternationalStandardsComments,
    ISCheckRequirements,
    StakeholderProposalComments
} from "../../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {DocumentDTO} from "../../../../core/store/data/levy/levy.model";
import swal from "sweetalert2";

@Component({
    selector: 'app-std-hop-tasks',
    templateUrl: './std-hop-tasks.component.html',
    styleUrls: ['./std-hop-tasks.component.css']
})
export class StdHopTasksComponent implements OnInit {
    p = 1;
    p2 = 1;
    tasks: DraftPublishing[] = [];
    approvedTasks: DraftPublishing[] = [];
    rejectedTasks: DraftPublishing[] = [];
    public actionRequest: DraftPublishing | undefined;
    public formActionRequest: StdTCDecision | undefined;
    fullname = '';
    title = 'toaster-not';
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    @ViewChild(DataTableDirective, {static: false})
    dtElement: DataTableDirective;
    isDtInitialized: boolean = false
    public itemId: number;
    loadingText: string;
    blob: Blob;
    public uploadedFiles:  FileList;
    public uploadedFile:  FileList;
    public uploadDrafts:  FileList;
    public uploadProofReads:  FileList;
    public uploadStandardFile:  FileList;

    displayedColumns: string[] = ['id', 'title', 'status', 'reason', 'actions'];
    dataSource!: MatTableDataSource<DraftPublishing>;
    dataSourceB!: MatTableDataSource<DraftPublishing>;
    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;

    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
    comStdRemarks: ComStdRemarks[] = [];
    internationalStandardsComments: InternationalStandardsComments[] = [];
    comStdCommitteeRemarks: ComStdCommitteeRemarks[] = [];
    isCheckRequirements:ISCheckRequirements[]=[];
    public actionRequests: ISCheckRequirements | undefined;
    approve: string;
    reject: string;
    isShowRemarksTabs= true;
    isShowRemarksTab= true;
    isShowCommentsTab= true;
    isShowCommentsTabs= true;
    isShowMainTab= true;
    isShowMainTabs= true;
    public uploadDraftStandardFormGroup!: FormGroup;
    public approveRequirementsFormGroup!: FormGroup;
    public rejectRequirementsFormGroup!: FormGroup;
    public editDraughtFormGroup!: FormGroup;
    public draughtFormGroup!: FormGroup;
    public proofReadFormGroup!: FormGroup;
    documentDTOs: DocumentDTO[] = [];


    constructor(private publishingService: PublishingService, private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService, private committeeService: CommitteeService,
                private stdComStandardService:StdComStandardService,
                private standardDevelopmentService : StandardDevelopmentService,
                private formBuilder: FormBuilder
    ) {
    }

    ngOnInit(): void {
        this.getHOPTasks();
        this.getApprovedTasks();
        this.getRejectedTasks();

        this.approve='Yes';
        this.reject='No';
        this.getComPublishingTasks();
        this.uploadDraftStandardFormGroup = this.formBuilder.group({
            id: [],
            requestId:[],
            draftId:[],
            title:[],
            scope:[],
            normativeReference:[],
            symbolsAbbreviatedTerms:[],
            clause:[],
            special:[],
            comStdNumber:[],
            preparedBy: [],
            docName:[],
            requestNumber:[],
            departmentId:[],
            subject:[],
            description:[],
            contactOneFullName:[],
            contactOneTelephone:[],
            contactOneEmail:[],
            contactTwoFullName:[],
            contactTwoTelephone:[],
            contactTwoEmail:[],
            contactThreeFullName:[],
            contactThreeTelephone:[],
            contactThreeEmail:[],
            companyName:[],
            companyPhone:[]

        });
        this.approveRequirementsFormGroup = this.formBuilder.group({
            id:[],
            comments: ['', Validators.required],
            accentTo: [],
            requestId:[],
            draftId:[],
            title:[],
            standardNumber:[]

        });

        this.rejectRequirementsFormGroup = this.formBuilder.group({
            id:[],
            comments: ['', Validators.required],
            accentTo: [],
            justificationId:[],
            requestId:[],
            draftId:[]

        });
        this.editDraughtFormGroup = this.formBuilder.group({
            id: [],
            requestId:[],
            title:[],
            docName:[],
            standardNumber:[],
            draughting:[],
            draftId:[],
            scope:[],
            normativeReference:[],
            symbolsAbbreviatedTerms:[],
            clause:[],
            special:[],
            comStdNumber:[],
            preparedBy: [],
            requestNumber:[],
            departmentId:[],
            subject:[],
            description:[],
            contactOneFullName:[],
            contactOneTelephone:[],
            contactOneEmail:[],
            contactTwoFullName:[],
            contactTwoTelephone:[],
            contactTwoEmail:[],
            contactThreeFullName:[],
            contactThreeTelephone:[],
            contactThreeEmail:[],
            companyName:[],
            companyPhone:[]

        });
        this.draughtFormGroup = this.formBuilder.group({
            id: [],
            requestId:[],
            title:[],
            docName:[],
            standardNumber:[],
            draughting:[],
            draftId:[],
            scope:[],
            normativeReference:[],
            symbolsAbbreviatedTerms:[],
            clause:[],
            special:[],
            comStdNumber:[],
            preparedBy: [],
            requestNumber:[],
            departmentId:[],
            subject:[],
            description:[],
            contactOneFullName:[],
            contactOneTelephone:[],
            contactOneEmail:[],
            contactTwoFullName:[],
            contactTwoTelephone:[],
            contactTwoEmail:[],
            contactThreeFullName:[],
            contactThreeTelephone:[],
            contactThreeEmail:[],
            companyName:[],
            companyPhone:[]

        });
        this.proofReadFormGroup = this.formBuilder.group({
            id: [],
            requestId:[],
            title:[],
            docName:[],
            standardNumber:[],
            draughting:[],
            draftId:[],
            scope:[],
            normativeReference:[],
            symbolsAbbreviatedTerms:[],
            clause:[],
            special:[],
            comStdNumber:[],
            preparedBy: [],
            requestNumber:[],
            departmentId:[],
            subject:[],
            description:[],
            contactOneFullName:[],
            contactOneTelephone:[],
            contactOneEmail:[],
            contactTwoFullName:[],
            contactTwoTelephone:[],
            contactTwoEmail:[],
            contactThreeFullName:[],
            contactThreeTelephone:[],
            contactThreeEmail:[],
            companyName:[],
            companyPhone:[]

        });

    }

    ngOnDestroy(): void {
        this.dtTrigger.unsubscribe();
        this.dtTrigger1.unsubscribe();
        this.dtTrigger2.unsubscribe();
    }


    public getHOPTasks(): void {
        this.loadingText = "Retrieving Data Please Wait ...."
        this.SpinnerService.show();
        this.publishingService.getHOPTasks().subscribe(
            (response: DraftPublishing[]) => {
                this.tasks = response;
                console.log(response)
                this.SpinnerService.hide();

                if (this.isDtInitialized) {
                    this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
                        dtInstance.destroy();
                        this.dtTrigger.next();
                    });
                } else {
                    this.isDtInitialized = true
                    this.dtTrigger.next();
                }
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.SpinnerService.hide();
            }
        );
    }

    public getApprovedTasks(): void {
        this.publishingService.getHOPTasksApproved().subscribe(
            (response: DraftPublishing[]) => {
                this.approvedTasks = response;
                this.dataSource = new MatTableDataSource(this.approvedTasks);

                this.dataSource.paginator = this.paginator;
                this.dataSource.sort = this.sort;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    public getRejectedTasks(): void {
        this.publishingService.getHOPTasksRejected().subscribe(
            (response: DraftPublishing[]) => {
                this.rejectedTasks = response;
                this.dataSourceB = new MatTableDataSource(this.rejectedTasks);

                this.dataSourceB.paginator = this.paginator;
                this.dataSourceB.sort = this.sort;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)

    }

    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)

    }

    public onOpenModal(task: DraftPublishing, mode: string): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        console.log(task.id);
        if (mode === 'edit') {
            this.actionRequest = task;
            button.setAttribute('data-target', '#justificationDecisionModal');
        }

        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    public decisionOnDraft(stdTCDecision: DraftPublishing, decision: string, reason: DraftPublishing): void {

        if (reason.varField1 != "") {
            this.loadingText = "Submitting ...."
            this.SpinnerService.show();
            stdTCDecision.varField1 = reason.varField1
            this.publishingService.decisionOnKSDraft(stdTCDecision, decision).subscribe(
                (response) => {
                    console.log(response);
                    this.SpinnerService.hide()
                    this.showToasterSuccess(response.httpStatus, `Successfully Submitted.`);
                    this.getHOPTasks();
                },
                (error: HttpErrorResponse) => {
                    alert(error.message);
                    this.SpinnerService.hide()

                }
            )
        } else {
            this.showToasterError("Error", `Please Provide A Reason`);

        }

    }

    @ViewChild('closeModal') private closeModal: ElementRef | undefined;

    public hideModel() {
        this.closeModal?.nativeElement.click();
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

    applyFilter(event: Event) {
        const filterValue = (event.target as HTMLInputElement).value;
        this.dataSource.filter = filterValue.trim().toLowerCase();
        this.dataSourceB.filter = filterValue.trim().toLowerCase();

        if (this.dataSource.paginator) {
            this.dataSource.paginator.firstPage();
        }
        if (this.dataSourceB.paginator) {
            this.dataSourceB.paginator.firstPage();
        }

    }

    public getComPublishingTasks(): void {
        this.loadingText = "Retrieving Drafts...";
        this.SpinnerService.show();
        this.stdComStandardService.getComPublishingTasks().subscribe(
            (response: ISCheckRequirements[]) => {
                this.isCheckRequirements = response;
                console.log(this.isCheckRequirements)
                this.rerender();
                this.SpinnerService.hide();

            },
            (error: HttpErrorResponse)=>{
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
    }
    toggleDisplayMainTab(){
        this.isShowMainTab = !this.isShowMainTab;
        this.isShowRemarksTab= true;
        this.isShowCommentsTab= true;
        this.isShowMainTabs= true;
    }
    toggleDisplayMainTabs(){
        this.isShowMainTabs = !this.isShowMainTabs;
        this.isShowMainTab= true;
        this.isShowRemarksTab= true;
        this.isShowCommentsTab= true;
    }


    toggleDisplayRemarksTab(requestId: number){
        this.loadingText = "Loading ...."
        this.SpinnerService.show();
        this.stdComStandardService.getAllComments(requestId).subscribe(
            (response: ComStdRemarks[]) => {
                this.comStdRemarks = response;
                this.SpinnerService.hide();
               // console.log(this.comStdRemarks)
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
        this.isShowCommentsTab = !this.isShowCommentsTab;
        this.isShowRemarksTab= true;
        this.isShowMainTab= true;
        this.isShowMainTabs= true;

    }
    displayDraftComments(draftID: number){
        //this.loadingText = "Loading ...."
        this.SpinnerService.show();
        this.stdComStandardService.getDraftComments(draftID).subscribe(
            (response: ComStdCommitteeRemarks[]) => {
                this.comStdCommitteeRemarks = response;
                this.SpinnerService.hide();
                console.log(this.comStdCommitteeRemarks)
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
        this.isShowRemarksTab = !this.isShowRemarksTab;
        this.isShowCommentsTab= true;
        this.isShowMainTab= true;
        this.isShowMainTabs= true;

    }

    public onOpenModals(iSCheckRequirement: ISCheckRequirements,mode:string,comStdDraftID: number): void{
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle','modal');
        this.stdComStandardService.getDraftDocumentList(comStdDraftID).subscribe(
            (response: DocumentDTO[]) => {
                this.documentDTOs = response;
                this.SpinnerService.hide();
                //console.log(this.documentDTOs)
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                //console.log(error.message);
            }
        );
        if (mode==='draftStandardForEditing') {
            this.actionRequests = iSCheckRequirement;
            button.setAttribute('data-target', '#draftStandardForEditing');
            this.uploadDraftStandardFormGroup.patchValue(
                {
                    id: this.actionRequests.id,
                    requestId: this.actionRequests.requestId,
                    draftId: this.actionRequests.draftId,
                    requestNumber: this.actionRequests.requestNumber,
                    title: this.actionRequests.title,
                    scope:this.actionRequests.scope,
                    normativeReference: this.actionRequests.normativeReference,
                    symbolsAbbreviatedTerms: this.actionRequests.symbolsAbbreviatedTerms,
                    clause:this.actionRequests.clause,
                    special:this.actionRequests.special,
                    comStdNumber:this.actionRequests.comStdNumber,
                    departmentId:this.actionRequests.departmentId,
                    subject:this.actionRequests.subject,
                    description:this.actionRequests.description,
                    contactOneFullName:this.actionRequests.contactOneFullName,
                    contactOneTelephone:this.actionRequests.contactOneTelephone,
                    contactOneEmail:this.actionRequests.contactOneEmail,
                    contactTwoFullName:this.actionRequests.contactTwoFullName,
                    contactTwoTelephone:this.actionRequests.contactTwoTelephone,
                    contactTwoEmail:this.actionRequests.contactTwoEmail,
                    contactThreeFullName:this.actionRequests.contactThreeFullName,
                    contactThreeTelephone:this.actionRequests.contactThreeTelephone,
                    contactThreeEmail:this.actionRequests.contactThreeEmail,
                    companyName:this.actionRequests.companyName,
                    companyPhone:this.actionRequests.companyPhone
                }
            );

        }
        if (mode==='checkRequirementsMet'){
            this.actionRequests=iSCheckRequirement;
            button.setAttribute('data-target','#checkRequirementsMet');

            this.approveRequirementsFormGroup.patchValue(
                {

                    requestId: this.actionRequests.requestId,
                    id: this.actionRequests.id,
                    draftId: this.actionRequests.draftId
                }
            );

        }
        if (mode==='draftStandardEditing'){
            this.actionRequests=iSCheckRequirement;
            button.setAttribute('data-target','#draftStandardEditing');

            this.editDraughtFormGroup.patchValue(
                {
                    requestId: this.actionRequests.requestId,
                    draftId: this.actionRequests.draftId,
                    id: this.actionRequests.id,
                    title: this.actionRequests.title,
                    docName:this.actionRequests.documentType,
                    standardNumber:this.actionRequests.comStdNumber,

                    requestNumber: this.actionRequests.requestNumber,
                    scope:this.actionRequests.scope,
                    normativeReference: this.actionRequests.normativeReference,
                    symbolsAbbreviatedTerms: this.actionRequests.symbolsAbbreviatedTerms,
                    clause:this.actionRequests.clause,
                    special:this.actionRequests.special,
                    comStdNumber:this.actionRequests.comStdNumber,
                    departmentId:this.actionRequests.departmentId,
                    subject:this.actionRequests.subject,
                    description:this.actionRequests.description,
                    contactOneFullName:this.actionRequests.contactOneFullName,
                    contactOneTelephone:this.actionRequests.contactOneTelephone,
                    contactOneEmail:this.actionRequests.contactOneEmail,
                    contactTwoFullName:this.actionRequests.contactTwoFullName,
                    contactTwoTelephone:this.actionRequests.contactTwoTelephone,
                    contactTwoEmail:this.actionRequests.contactTwoEmail,
                    contactThreeFullName:this.actionRequests.contactThreeFullName,
                    contactThreeTelephone:this.actionRequests.contactThreeTelephone,
                    contactThreeEmail:this.actionRequests.contactThreeEmail,
                    companyName:this.actionRequests.companyName,
                    companyPhone:this.actionRequests.companyPhone
                }
            );

        }
        if (mode==='standardDrafting'){
            this.actionRequests=iSCheckRequirement;
            button.setAttribute('data-target','#standardDrafting');

            this.draughtFormGroup.patchValue(
                {
                    requestId: this.actionRequests.requestId,
                    draftId: this.actionRequests.draftId,
                    id: this.actionRequests.id,
                    title: this.actionRequests.title,
                    docName:this.actionRequests.documentType,
                    standardNumber:this.actionRequests.comStdNumber,

                    requestNumber: this.actionRequests.requestNumber,
                    scope:this.actionRequests.scope,
                    normativeReference: this.actionRequests.normativeReference,
                    symbolsAbbreviatedTerms: this.actionRequests.symbolsAbbreviatedTerms,
                    clause:this.actionRequests.clause,
                    special:this.actionRequests.special,
                    comStdNumber:this.actionRequests.comStdNumber,
                    departmentId:this.actionRequests.departmentId,
                    subject:this.actionRequests.subject,
                    description:this.actionRequests.description,
                    contactOneFullName:this.actionRequests.contactOneFullName,
                    contactOneTelephone:this.actionRequests.contactOneTelephone,
                    contactOneEmail:this.actionRequests.contactOneEmail,
                    contactTwoFullName:this.actionRequests.contactTwoFullName,
                    contactTwoTelephone:this.actionRequests.contactTwoTelephone,
                    contactTwoEmail:this.actionRequests.contactTwoEmail,
                    contactThreeFullName:this.actionRequests.contactThreeFullName,
                    contactThreeTelephone:this.actionRequests.contactThreeTelephone,
                    contactThreeEmail:this.actionRequests.contactThreeEmail,
                    companyName:this.actionRequests.companyName,
                    companyPhone:this.actionRequests.companyPhone
                }
            );

        }
        if (mode==='standardProofreading'){
            this.actionRequests=iSCheckRequirement;
            button.setAttribute('data-target','#standardProofreading');

            this.proofReadFormGroup.patchValue(
                {
                    requestId: this.actionRequests.requestId,
                    draftId: this.actionRequests.draftId,
                    id: this.actionRequests.id,
                    title: this.actionRequests.title,
                    docName:this.actionRequests.documentType,
                    standardNumber:this.actionRequests.comStdNumber,
                    draughting:this.actionRequests.draughting,
                    requestNumber: this.actionRequests.requestNumber,
                    scope:this.actionRequests.scope,
                    normativeReference: this.actionRequests.normativeReference,
                    symbolsAbbreviatedTerms: this.actionRequests.symbolsAbbreviatedTerms,
                    clause:this.actionRequests.clause,
                    special:this.actionRequests.special,
                    comStdNumber:this.actionRequests.comStdNumber,
                    departmentId:this.actionRequests.departmentId,
                    subject:this.actionRequests.subject,
                    description:this.actionRequests.description,
                    contactOneFullName:this.actionRequests.contactOneFullName,
                    contactOneTelephone:this.actionRequests.contactOneTelephone,
                    contactOneEmail:this.actionRequests.contactOneEmail,
                    contactTwoFullName:this.actionRequests.contactTwoFullName,
                    contactTwoTelephone:this.actionRequests.contactTwoTelephone,
                    contactTwoEmail:this.actionRequests.contactTwoEmail,
                    contactThreeFullName:this.actionRequests.contactThreeFullName,
                    contactThreeTelephone:this.actionRequests.contactThreeTelephone,
                    contactThreeEmail:this.actionRequests.contactThreeEmail,
                    companyName:this.actionRequests.companyName,
                    companyPhone:this.actionRequests.companyPhone
                }
            );

        }
        if (mode==='approveChanges'){
            this.actionRequests=iSCheckRequirement;
            button.setAttribute('data-target','#approveChanges');

            this.approveRequirementsFormGroup.patchValue(
                {
                    accentTo: this.approve,
                    requestId: this.actionRequests.requestId,
                    id: this.actionRequests.id,
                    draftId: this.actionRequests.draftId
                }
            );
            this.rejectRequirementsFormGroup.patchValue(
                {
                    accentTo: this.reject,
                    requestId: this.actionRequests.requestId,
                    justificationId: this.actionRequests.justificationNo,
                    draftId: this.actionRequests.id
                }
            );
        }
        if (mode==='approveEditedDraft'){
            this.actionRequests=iSCheckRequirement;
            button.setAttribute('data-target','#approveEditedDraft');

            this.approveRequirementsFormGroup.patchValue(
                {
                    accentTo: this.approve,
                    requestId: this.actionRequests.requestId,
                    id: this.actionRequests.id,
                    draftId: this.actionRequests.draftId,
                    title: this.actionRequests.title,
                    standardNumber: this.actionRequests.comStdNumber
                }
            );
            this.rejectRequirementsFormGroup.patchValue(
                {
                    accentTo: this.reject,
                    requestId: this.actionRequests.requestId,
                    justificationId: this.actionRequests.justificationNo,
                    draftId: this.actionRequests.id
                }
            );
        }
        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    @ViewChild('closeModalStdEditing') private closeModalStdEditing: ElementRef | undefined;

    public hideModalStdEditing() {
        this.closeModalStdEditing?.nativeElement.click();
    }

    @ViewChild('closeModalRequirements') private closeModalRequirements: ElementRef | undefined;

    public hideModalRequirements() {
        this.closeModalRequirements?.nativeElement.click();
    }

    @ViewChild('closeModalDraftEditing') private closeModalDraftEditing: ElementRef | undefined;

    public hideModalDraftEditing() {
        this.closeModalDraftEditing?.nativeElement.click();
    }

    @ViewChild('closeModalDrafting') private closeModalDrafting: ElementRef | undefined;

    public hideModalDrafting() {
        this.closeModalDrafting?.nativeElement.click();
    }

    @ViewChild('closeModalProofReading') private closeModalProofReading: ElementRef | undefined;

    public hideModalProofReading() {
        this.closeModalProofReading?.nativeElement.click();
    }

    @ViewChild('closeModalChanges') private closeModalChanges: ElementRef | undefined;

    public hideModalChanges() {
        this.closeModalChanges?.nativeElement.click();
    }

    @ViewChild('closeModalEditedDraft') private closeModalEditedDraft: ElementRef | undefined;

    public hideModalEditedDraft() {
        this.closeModalEditedDraft?.nativeElement.click();
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
            this.dtTrigger1.next();
            this.dtTrigger2.next();
        });
    }

    viewDraftFile(pdfId: number, fileName: string, applicationType: string): void {
        this.SpinnerService.show();
        this.stdComStandardService.viewCompanyDraft(pdfId).subscribe(
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
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Processing Request`);
                console.log(error.message);
                this.getComPublishingTasks();
                //alert(error.message);
            }
        );
    }

    submitDraftForEditing(): void {
        this.loadingText = "Saving...";
        this.SpinnerService.show();
        this.stdComStandardService.submitDraftForEditing(this.uploadDraftStandardFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.onClickSaveUploads(response.body.draftId)
                  this.uploadDraftStandardFormGroup.reset();
                this.getComPublishingTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Draft Prepared`);

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalStdEditing();

    }

    onClickSaveUploads(comStdDraftID: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.stdComStandardService.uploadPDFileDetails(comStdDraftID, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFiles = null;
                    console.log(data);
                    swal.fire({
                        title: 'Thank you....',
                        html:'Company Standard Draft Uploaded',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                },
            );

        }

    }

    onClickSaveUpload(comStdDraftID: string) {
        if (this.uploadedFile.length > 0) {
            const file = this.uploadedFile;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.stdComStandardService.uploadPDFileDetails(comStdDraftID, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFile = null;
                    console.log(data);
                    swal.fire({
                        title: 'Thank you....',
                        html:'Company Standard Draft Uploaded',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                },
            );

        }

    }

    onClickUploadDraft(comStdDraftID: string) {
        if (this.uploadDrafts.length > 0) {
            const file = this.uploadDrafts;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.stdComStandardService.uploadPDFileDetails(comStdDraftID, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadDrafts = null;
                    console.log(data);
                    swal.fire({
                        title: 'Thank you....',
                        html:'Company Standard Draft Uploaded',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                },
            );

        }

    }

    onClickUploadDrafts(comStdDraftID: string) {
        if (this.uploadProofReads.length > 0) {
            const file = this.uploadProofReads;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.stdComStandardService.uploadPDFileDetails(comStdDraftID, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadProofReads = null;
                    console.log(data);
                    swal.fire({
                        title: 'Thank you....',
                        html:'Company Standard Draft Uploaded',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                },
            );

        }

    }

    onClickUploadStandard(standardID: string) {
        if (this.uploadStandardFile.length > 0) {
            const file = this.uploadStandardFile;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.stdComStandardService.uploadCompanyStandard(standardID, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadStandardFile = null;
                    console.log(data);
                    swal.fire({
                        title: 'Thank you....',
                        html:'Company Standard Uploaded',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                },
            );

        }

    }

    approveRequirements(): void {
        this.loadingText = "Approving Draft...";
        this.SpinnerService.show();
        this.stdComStandardService.checkRequirements(this.approveRequirementsFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getComPublishingTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Draft Approved`);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalRequirements();
    }

    rejectRequirements(): void {
        this.loadingText = "Rejecting Draft...";
        this.SpinnerService.show();
        this.stdComStandardService.checkRequirements(this.rejectRequirementsFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getComPublishingTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Draft Rejected`);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalRequirements();
    }

    editStandardDraft(): void {
        this.loadingText = "Saving Draft...";
        this.SpinnerService.show();
        console.log(this.editDraughtFormGroup.value);
        this.stdComStandardService.editStandardDraft(this.editDraughtFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getComPublishingTasks();
                this.onClickSaveUpload(response.body.draftId)
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Draft Updated`);
                this.editDraughtFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalDraftEditing();
    }
    draughtStandard(): void {
        this.loadingText = "Approving Draft...";
        this.SpinnerService.show();
        console.log(this.draughtFormGroup);
        this.stdComStandardService.draughtStandard(this.draughtFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getComPublishingTasks();
                this.onClickUploadDraft(response.body.draftId)
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Draft Updated`);
                this.editDraughtFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalDrafting();
    }

    proofReadStandard(): void {
        this.loadingText = "Approving Draft...";
        this.SpinnerService.show();
        this.stdComStandardService.proofReadStandard(this.proofReadFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getComPublishingTasks();
                this.onClickUploadDrafts(response.body.draftId)
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Draft Updated`);
                this.editDraughtFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalProofReading();
    }
    approveProofReadStandard(): void {
        this.loadingText = "Approving Draft...";
        this.SpinnerService.show();
        this.stdComStandardService.approveProofReadStandard(this.approveRequirementsFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getComPublishingTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Draft Approved`);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalChanges();
    }

    approveEditedDraft(): void {
        this.loadingText = "Approving Edited Draft...";
        this.SpinnerService.show();
        this.stdComStandardService.approveEditedDraft(this.approveRequirementsFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.onClickUploadStandard(response.body.id)
                this.getComPublishingTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Draft Approved`);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalEditedDraft();
    }

    rejectCompanyStandard(): void {
        this.loadingText = "Rejecting Edited Draft...";
        this.SpinnerService.show();
        this.stdComStandardService.rejectCompanyStandard(this.approveRequirementsFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getComPublishingTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Draft Rejected`);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalEditedDraft();
    }

}
