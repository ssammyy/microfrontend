import { Component, OnInit } from '@angular/core';
import { FileService } from 'src/app/core/services/file.service';
import * as fileSaver from 'file-saver';

@Component({
  selector: 'app-di-cor',
  templateUrl: './di-cor.component.html',
  styleUrls: ['./di-cor.component.css']
})
export class DiCorComponent implements OnInit {
  active = 'cor-details';
  constructor(private fileService: FileService) { }

  ngOnInit(): void {
  }
  downloadCorFile(): void {
    this.fileService.downloadFile().subscribe(response => {
      let blob: any = new Blob([response], { type: 'application/pdf' });
      const url = window.URL.createObjectURL(blob);
      //window.open(url);
      //window.location.href = response.url;
      fileSaver.saveAs(blob, 'davy.pdf');
    }), error => console.log('Error downloading the file'),
      () => console.info('File downloaded successfully');

  }

}
