package forms;

import javax.validation.Valid;

import org.hibernate.annotations.Type;

import domain.Conference;
import domain.Submission;

public class SubmissionForm {

	/* Attributes */

	/* Submission attributes */
	private int id;
	private int version;
	private Conference conference;

	/* Paper attributes */
	private String titleP;
	private String authorsP;
	private String summaryP;
	private String paperDocumentP;
	
	/* Paper Camera-Ready attributes */
	private String titlePCR;
	private String authorsPCR;
	private String summaryPCR;
	private String paperDocumentPCR;

	/* Constructors */
	public SubmissionForm() {

	}
	
	public SubmissionForm(Submission submission) {
		this.id = submission.getId();
		this.version = submission.getVersion();
		this.conference = submission.getConference();
		this.titleP = submission.getPaper().getTitle();
		this.authorsP = submission.getPaper().getAuthors();
		this.summaryP = submission.getPaper().getSummary();
		this.paperDocumentP = submission.getPaper().getPaperDocument();
	}

	/* Getters and Setters*/
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	@Valid 
	public Conference getConference() {
		return conference;
	}
	public void setConference(Conference conference) {
		this.conference = conference;
	}

	public String getTitleP() {
		return titleP;
	}

	public void setTitleP(String titleP) {
		this.titleP = titleP;
	}

	@Type(type="text")
	public String getAuthorsP() {
		return authorsP;
	}

	public void setAuthorsP(String authorsP) {
		this.authorsP = authorsP;
	}

	@Type(type="text")
	public String getSummaryP() {
		return summaryP;
	}

	public void setSummaryP(String summaryP) {
		this.summaryP = summaryP;
	}

	@Type(type="text")
	public String getPaperDocumentP() {
		return paperDocumentP;
	}

	public void setPaperDocumentP(String paperDocumentP) {
		this.paperDocumentP = paperDocumentP;
	}

	public String getTitlePCR() {
		return titlePCR;
	}

	public void setTitlePCR(String titlePCR) {
		this.titlePCR = titlePCR;
	}

	@Type(type="text")
	public String getAuthorsPCR() {
		return authorsPCR;
	}

	public void setAuthorsPCR(String authorsPCR) {
		this.authorsPCR = authorsPCR;
	}

	@Type(type="text")
	public String getSummaryPCR() {
		return summaryPCR;
	}

	public void setSummaryPCR(String summaryPCR) {
		this.summaryPCR = summaryPCR;
	}

	@Type(type="text")
	public String getPaperDocumentPCR() {
		return paperDocumentPCR;
	}

	public void setPaperDocumentPCR(String paperDocumentPCR) {
		this.paperDocumentPCR = paperDocumentPCR;
	}

}