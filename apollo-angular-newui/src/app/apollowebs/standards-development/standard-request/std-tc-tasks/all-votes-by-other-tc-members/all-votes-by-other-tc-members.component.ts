import {Component, ElementRef, Input, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {
    Document,
    LiaisonOrganization,
    NwiItem,
    NWIsForVoting,
    StandardRequestB,
    Stdtsectask
} from "../../../../../core/store/data/std/request_std.model";
import {VoteNwiRetrieved, VotesNwiTally} from "../../../../../core/store/data/std/commitee-model";
import {StandardsDto, UserDetails} from "../../../../../core/store/data/master/master.model";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {UsersEntity} from "../../../../../core/store/data/std/std.model";
import {IDropdownSettings} from "ng-multiselect-dropdown";
import {StandardDevelopmentService} from "../../../../../core/store/data/std/standard-development.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../../core/store/data/std/notification.service";
import {CommitteeService} from "../../../../../core/store/data/std/committee.service";
import {ListItem} from "ng-multiselect-dropdown/multiselect.model";
import {HttpErrorResponse} from "@angular/common/http";
import {formatDate} from "@angular/common";

@Component({
    selector: 'app-all-votes-by-other-tc-members',
    templateUrl: './all-votes-by-other-tc-members.component.html',
    styleUrls: ['./all-votes-by-other-tc-members.component.css']
})
export class AllVotesByOtherTcMembersComponent implements OnInit {

    dtOptions: DataTables.Settings = {};
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
    dtTrigger3: Subject<any> = new Subject<any>();
    dtTrigger4: Subject<any> = new Subject<any>();
    dtTrigger5: Subject<any> = new Subject<any>();
    dtTrigger6: Subject<any> = new Subject<any>();
    p = 1;
    p2 = 1;

    docs !: Document[];
    blob: Blob;
    voteRetrieved !: VoteNwiRetrieved[];
    tcPrincipalMembers !: UserDetails[];


    public actionRequest: NWIsForVoting | undefined;

    dateFormat = "yyyy-MM-dd";
    language = "en";

    public itemId: string = "";
    public secTasks: StandardRequestB[] = [];
    public tscsecRequest !: Stdtsectask | undefined;
    public nwiItem!: NwiItem[];
    public nwiForVotes: VotesNwiTally[] = [];

    loadedStandards: StandardsDto[] = [];
    loading = false;
    loadingText: string;
    public uploadedFilesC: Array<File> = [];
    validTextType: boolean = false;
    validNumberType: boolean = false;
    public nwiRequest !: StandardRequestB | undefined;
    selectedNwi: string;


    constructor(
        private formBuilder: FormBuilder,
        private standardDevelopmentService: StandardDevelopmentService,
        private SpinnerService: NgxSpinnerService,
        private notifyService: NotificationService,
        private committeeService: CommitteeService,
    ) {
    }

    ngOnInit(): void {
        this.getAllNwisUnderVote();


    }


    public getAllNwisUnderVote(): void {
        this.standardDevelopmentService.getAllVotesTallyOtherTcMembers().subscribe(
            (response: VotesNwiTally[]) => {
                this.nwiForVotes = response;
                this.rerender()
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

    @ViewChild('closeModalB') private closeModalB: ElementRef | undefined;

    public hideModal() {
        this.closeModalB?.nativeElement.click();
    }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)

    }

    public onOpenModal(secTask: StandardRequestB, mode: string): void {


        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    public onOpenModalVote(task: VotesNwiTally, mode: string): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');

        if (mode === 'edit') {

            this.getSpecificNwi(String(task.nwi_ID))
            this.getAllDocs(String(task.nwi_ID))

            button.setAttribute('data-target', '#viewNwi');
        }
        if (mode === 'viewVotes') {
            this.getAllVotes(task.nwi_ID)

            button.setAttribute('data-target', '#viewVotes');
        }
        // @ts-ignore
        container.appendChild(button);
        button.click();

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
            this.dtTrigger4.next();
            this.dtTrigger5.next();
            this.dtTrigger6.next();

        });

    }

    ngOnDestroy(): void {
        // Do not forget to unsubscribe the event
        this.dtTrigger1.unsubscribe();
        this.dtTrigger2.unsubscribe();
        this.dtTrigger3.unsubscribe();
        this.dtTrigger4.unsubscribe();
        this.dtTrigger5.unsubscribe();
        this.dtTrigger6.unsubscribe();

    }

    formatFormDate(date: Date) {
        return formatDate(date, this.dateFormat, this.language);
    }

    public getAllDocs(nwiId: string): void {
        this.standardDevelopmentService.getAdditionalDocumentsByProcess(nwiId, "NWI Documents").subscribe(
            (response: Document[]) => {
                this.docs = response;
                this.rerender()


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    public getSpecificNwi(nwiId: string): void {
        this.standardDevelopmentService.getNwiById(nwiId).subscribe(
            (response: NwiItem[]) => {
                this.nwiItem = response;

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        )
    }

    viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
        this.SpinnerService.show();
        this.committeeService.viewDocsById(pdfId).subscribe(
            (dataPdf: any) => {
                this.SpinnerService.hide();
                this.blob = new Blob([dataPdf], {type: applicationType});

                // tslint:disable-next-line:prefer-const
                let downloadURL = window.URL.createObjectURL(this.blob);
                window.open(downloadURL, '_blank');

                // this.pdfUploadsView = dataPdf;
            },
        );
    }

    private getAllVotes(nwiId: number) {
        this.standardDevelopmentService.getAllVotesOnNwi(nwiId).subscribe(
            (response: VoteNwiRetrieved[]) => {

                this.voteRetrieved = response;
                this.rerender()

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }


    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)

    }


}
