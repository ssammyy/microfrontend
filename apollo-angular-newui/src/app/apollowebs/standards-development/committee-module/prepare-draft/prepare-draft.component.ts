// import {Component, OnInit, ViewChild} from '@angular/core';
// import {Router} from "@angular/router";
// import {NwiItem} from "../../../../core/store/data/std/request_std.model";
// import {HttpErrorResponse} from "@angular/common/http";
// import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
// import {FormBuilder, FormGroup, Validators} from "@angular/forms";
// // import {DocumentEditorContainerComponent, ToolbarService} from '@syncfusion/ej2-angular-documenteditor';
// import {isNullOrUndefined} from "@swimlane/ngx-datatable";
// // import {TitleBar} from "../../../title-bar";
// import {CommitteeService} from "../../../../core/store/data/std/committee.service";
// import {ApiEndpointService} from "../../../../core/services/endpoints/api-endpoint.service";
//
// @Component({
//     selector: 'app-prepare-draft',
//     templateUrl: './prepare-draft.component.html',
//     styleUrls: ['./prepare-draft.component.css'],
//     // providers: [ToolbarService]
//
// })
// export class PrepareDraftComponent implements OnInit {
//     id: string;
//     public nwiItem!: NwiItem[];
//     step = 1;
//     ksNumber: string;
//     introductoryDetails: FormGroup;
//     // @ViewChild('documenteditor_ref') public container!: DocumentEditorContainerComponent;
//     public serviceLink: string;
//     // titleBar: TitleBar;
//     blob: Blob;
//     protocol = `https://`;
//     baseUrl = ApiEndpointService.DOMAIN.LOCAL_DEV
//
//     private apiServerUrl = `${this.protocol}${this.baseUrl}/api/v1/migration/committee/`;
//
//
//     constructor(private router: Router, private committeeService: CommitteeService,
//                 private standardDevelopmentService: StandardDevelopmentService, private formBuilder: FormBuilder,
//     ) {
//
//         this.serviceLink = `${this.apiServerUrl}`
//     }
//
//
//     ngOnInit(): void {
//         this.checkForId();
//         this.getSpecificNwi(this.id)
//         this.introductoryDetailsForm()
//
//     }
//
//
//     private checkForId() {
//         if (localStorage.getItem('id') != null) {
//             this.id = localStorage.getItem('id');
//
//         } else {
//             this.router.navigate(['/preparePd']);
//
//         }
//     }
//
//
//     public getSpecificNwi(nwiId: string): void {
//         this.standardDevelopmentService.getNwiById(nwiId).subscribe(
//             (response: NwiItem[]) => {
//                 this.nwiItem = response;
//                 for (let nwiItem of response) {
//                     this.ksNumber = nwiItem.referenceNumber
//                 }
//             },
//             (error: HttpErrorResponse) => {
//                 alert(error.message);
//             }
//         )
//     }
//
//
//     public introductoryDetailsForm(): void {
//         this.introductoryDetails = this.formBuilder.group({
//             ks: ['', Validators.required],
//             ics: ['', Validators.required],
//             edition: ['', Validators.required],
//             introductoryElement: ['', Validators.required],
//             partTitle: ['', Validators.required],
//             organisation: ['', Validators.required],
//             foreword: ['', Validators.required],
//
//
//         });
//     }
//
//     onClickSaveSTA1(valid: boolean) {
//         if (valid) {
//             console.log(valid)
//         } else {
//             console.log(valid)
//
//         }
//     }
//
//     // onCreate(): void {
//     //     let titleBarElement: HTMLElement = document.getElementById('default_title_bar');
//     //     this.titleBar = new TitleBar(titleBarElement, this.container.documentEditor, true);
//     //     //Opens the default template Getting Started.docx from web API.
//     //     this.openTemplate();
//     //     this.container.documentEditor.documentName = 'Getting Started';
//     //     this.titleBar.updateDocumentTitle();
//     //     //Sets the language id as EN_US (1033) for spellchecker and docker image includes this language dictionary by default.
//     //     //The spellchecker ensures the document content against this language.
//     //     this.container.documentEditor.spellChecker.languageID = 1033;
//     //     setInterval(() => {
//     //         this.updateDocumentEditorSize();
//     //     }, 100);
//     //     //Adds event listener for browser window resize event.
//     //     window.addEventListener("resize", this.onWindowResize);
//     // }
//     //
//     // onDocumentChange(): void {
//     //     if (!isNullOrUndefined(this.titleBar)) {
//     //         this.titleBar.updateDocumentTitle();
//     //     }
//     //     this.container.documentEditor.focusIn();
//     // }
//
//     onDestroy(): void {
//         //Removes event listener for browser window resize event.
//         window.removeEventListener("resize", this.onWindowResize);
//     }
//
//     onWindowResize = (): void => {
//         //Resizes the document editor component to fit full browser window automatically whenever the browser resized.
//         this.updateDocumentEditorSize();
//     }
//     //
//     // updateDocumentEditorSize(): void {
//     //     //Resizes the document editor component to fit full browser window.
//     //     const windowWidth = window.innerWidth;
//     //     //Reducing the size of title bar, to fit Document editor component in remaining height.
//     //     const windowHeight = window.innerHeight - this.titleBar.getHeight();
//     //     this.container.resize(windowWidth, windowHeight);
//     // }
//
//     openTemplate(): void {
//         const uploadDocument = new FormData();
//         uploadDocument.append('docId', '595');
//         var params = "docId=595";
//
//         const loadDocumentUrl = this.serviceLink + 'viewByIdB';
//         const httpRequest = new XMLHttpRequest();
//         httpRequest.open('GET', loadDocumentUrl + "?" + params, true);
//         const dataContext = this;
//         // httpRequest.onreadystatechange = function () {
//         //     if (httpRequest.readyState === 4) {
//         //         if (httpRequest.status === 200 || httpRequest.status === 304) {
//         //             //Opens the SFDT for the specified file received from the web API.
//         //             dataContext.container.documentEditor.open(httpRequest.responseText);
//         //         }
//         //     }
//         // };
//         // //Sends the request with template file name to web API.
//         // httpRequest.send(uploadDocument);
//
//         this.committeeService.viewDocsByIdB(595).subscribe(
//             (response: any) => {
//
//                 console.log(response.body)
//                 this.serviceLink = response;
//
//
//                 dataContext.container.documentEditor.open(response.body);
//             },
//         );
//
//
//     }
//
//
// }
