import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-coc-certificates',
  templateUrl: './coc-certificates.component.html',
  styleUrls: ['./coc-certificates.component.css']
})
export class CocCertificatesComponent implements OnInit {
  documentType = "coc"
  constructor() { }

  ngOnInit(): void {
  }

}
