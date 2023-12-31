package com.myspace.corporatelos.prescreening;

/**
 * This class was automatically generated by the data modeler tool.
 */

public class PrescreeningScripts implements java.io.Serializable {

	static final long serialVersionUID = 1L;

	public PrescreeningScripts() {
	}

	public static void checkEntityBasics(
			org.kie.api.runtime.process.ProcessContext kcontext) {
		try {
			org.json.JSONObject partyResp = new org.json.JSONObject(kcontext
					.getVariable("partyResponse").toString());
			System.out.println("party Resp: " + partyResp);
			if (!partyResp.isNull("firstName")) {
				kcontext.setVariable("nameCheck", true);
			} else {
				kcontext.setVariable("nameCheck", false);
			}
			org.json.JSONArray partyArray = partyResp.getJSONArray("addresses");
			for (int i = 0; i < partyArray.length(); i++) {
				org.json.JSONObject indEle = new org.json.JSONObject(partyArray
						.get(i).toString());
				System.out.println("Addresses *****" + indEle.toString());
				if (!indEle.isNull("phoneNo")) {
					kcontext.setVariable("addressCheck", true);
					break;
				} else {
					kcontext.setVariable("addressCheck", false);
				}
			}

			org.json.JSONObject relationResp = new org.json.JSONObject(kcontext
					.getVariable("contactResponse").toString());
			System.out.println("Relations: ****" + relationResp.toString());
			if (relationResp.has("partyRelations")) {
				org.json.JSONArray relationsArray = relationResp
						.getJSONArray("partyRelations");
				for (int i = 0; i < relationsArray.length(); i++) {
					org.json.JSONObject indEle = new org.json.JSONObject(
							relationsArray.get(i).toString());
					System.out.println("ind relation: " + indEle.toString());
					if (indEle.get("relationType").equals("Legal")) {
						kcontext.setVariable("relationCheck", true);
						break;
					} else {
						kcontext.setVariable("relationCheck", false);
					}
				}
			} else {
				kcontext.setVariable("relationCheck", false);
			}
			Boolean address = (Boolean) kcontext.getVariable("addressCheck");
			Boolean name = (Boolean) kcontext.getVariable("nameCheck");
			Boolean relation = (Boolean) kcontext.getVariable("relationCheck");
			System.out.println(name + " " + address + " " + relation);
			if (address && name && relation) {
				kcontext.setVariable("result", true);
			} else {
				kcontext.setVariable("result", false);
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		}
	}

	public static void checkEntityStatus(
			org.kie.api.runtime.process.ProcessContext kcontext) {
		try {
			System.out.println("response: "
					+ kcontext.getVariable("response").toString());
			org.json.JSONObject responseObj = new org.json.JSONObject(kcontext
					.getVariable("response").toString());
			if (!responseObj.get("partyStatus").equals("Watchlist")) {
				kcontext.setVariable("result", true);
			} else {
				kcontext.setVariable("result", false);
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		}
	}

	public static void checkAddressInfo(
			org.kie.api.runtime.process.ProcessContext kcontext) {
		try {
			org.json.JSONObject addressResp = new org.json.JSONObject(kcontext
					.getVariable("response").toString());
			org.json.JSONArray addressArray = addressResp
					.getJSONArray("addresses");
			for (int i = 0; i < addressArray.length(); i++) {
				org.json.JSONObject indEle = new org.json.JSONObject(
						addressArray.get(i).toString());
				kcontext.setVariable("addressTypeCheck",
						(Boolean) indEle.get("primary"));
				System.out.println("primary flag: "
						+ kcontext.getVariable("addressTypeCheck"));
				if (!indEle.isNull("countryCode")
						&& !indEle.isNull("postalOrZipCode")) {
					kcontext.setVariable("addressInfoCheck", true);
					break;
				} else {
					kcontext.setVariable("addressInfoCheck", false);
				}
			}
			Boolean infoCheck = (Boolean) kcontext
					.getVariable("addressInfoCheck");
			Boolean typeCheck = (Boolean) kcontext
					.getVariable("addressTypeCheck");
			if (infoCheck && typeCheck) {
				kcontext.setVariable("result", true);
			} else {
				kcontext.setVariable("result", false);
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		}
	}

	public static void checkPartyRelation(
			org.kie.api.runtime.process.ProcessContext kcontext) {
		try {
			org.json.JSONObject relationResp = new org.json.JSONObject(kcontext
					.getVariable("partyRelationsResponse").toString());
			if (relationResp.has("partyRelations")) {
				org.json.JSONArray relationsArray = relationResp
						.getJSONArray("partyRelations");
				if (relationsArray.length() > 0) {
					for (int i = 0; i < relationsArray.length(); i++) {
						org.json.JSONObject indEle = new org.json.JSONObject(
								relationsArray.get(i).toString());
						if (indEle.get("relationType").equals("Legal")) {
							kcontext.setVariable("relationCheck", true);
							break;
						} else {
							kcontext.setVariable("relationCheck", false);
						}
					}
				} else {
					kcontext.setVariable("relationCheck", false);
				}
			} else {
				kcontext.setVariable("relationCheck", false);
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		}
	}

	public static void checkContactReferences(
			org.kie.api.runtime.process.ProcessContext kcontext) {
		try {
			org.json.JSONObject partyResp = new org.json.JSONObject(kcontext
					.getVariable("partyResponse").toString());
			System.out.println("party Resp: " + partyResp);
			org.json.JSONArray contactArray = partyResp
					.getJSONArray("contactReferences");
			if (contactArray.length() > 0) {
				for (int i = 0; i < contactArray.length(); i++) {
					System.out.println("Contact Resp: "
							+ contactArray.get(i).toString());
					org.json.JSONObject indContact = new org.json.JSONObject(
							contactArray.get(i).toString());
					if (indContact.get("contactTypePurpose").equals("Primary")
							&& !indContact.isNull("jobTitle")) {
						kcontext.setVariable("hasContact", true);
						System.out.println(indContact.get("contactPartyRef")
								.toString());
						kcontext.setVariable("contactPartyRef",
								indContact.get("contactPartyRef").toString());
						break;
					} else {
						kcontext.setVariable("hasContact", false);
					}
				}
			} else {
				kcontext.setVariable("hasContact", false);
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		}
	}

	public static void checkContactInfo(
			org.kie.api.runtime.process.ProcessContext kcontext) {
		try {
			org.json.JSONObject partyResp = new org.json.JSONObject(kcontext
					.getVariable("partyResponse").toString());
			System.out.println("party Resp: " + partyResp);
			if (!partyResp.isNull("firstName") && !partyResp.isNull("lastName")) {
				kcontext.setVariable("nameCheck", true);
			} else {
				kcontext.setVariable("nameCheck", false);
			}
			org.json.JSONArray partyArray = partyResp.getJSONArray("addresses");
			if (partyArray.length() > 0) {
				for (int i = 0; i < partyArray.length(); i++) {
					org.json.JSONObject indEle = new org.json.JSONObject(
							partyArray.get(i).toString());
					if (!indEle.isNull("phoneNo")) {
						kcontext.setVariable("phoneCheck", true);
						break;
					} else {
						kcontext.setVariable("phoneCheck", false);
					}
				}

				for (int i = 0; i < partyArray.length(); i++) {
					org.json.JSONObject indEle = new org.json.JSONObject(
							partyArray.get(i).toString());
					if (!indEle.isNull("electronicAddress")) {
						kcontext.setVariable("emailCheck", true);
						break;
					} else {
						kcontext.setVariable("emailCheck", false);
					}
				}
			} else {
				kcontext.setVariable("phoneCheck", false);
				kcontext.setVariable("emailCheck", false);
			}
			Boolean name = (Boolean) kcontext.getVariable("nameCheck");
			Boolean phone = (Boolean) kcontext.getVariable("phoneCheck");
			Boolean email = (Boolean) kcontext.getVariable("emailCheck");

			if (name && email && phone) {
				kcontext.setVariable("hasData", true);
			} else {
				kcontext.setVariable("hasData", false);
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		}
	}

	public static void checkFacilityStructure(
			org.kie.api.runtime.process.ProcessContext kcontext) {
		try {
			org.json.JSONObject facilitiesObj = new org.json.JSONObject(
					kcontext.getVariable("response").toString());//
			if (!facilitiesObj.isNull("totalAmount")
					&& (!facilitiesObj.isNull("year")
							|| !facilitiesObj.isNull("month") || !facilitiesObj
								.isNull("days"))) {
				kcontext.setVariable("result", true);
			} else {
				kcontext.setVariable("result", false);
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		}
	}

	public static void prepareProcessesList(
			org.kie.api.runtime.process.ProcessContext kcontext) {
		try {
			boolean modifiedFlag = Boolean.parseBoolean(kcontext.getVariable(
					"isModifiedFacility").toString());
			java.util.ArrayList procList = new java.util.ArrayList();
			if (!modifiedFlag) {
				if (kcontext.getVariable("isIndividualParty").toString()
						.equals("false")) {
					procList.add("CorporateLOS.BusinessBasics");
					procList.add("CorporateLOS.CompanyInfoCheck");
				}
				procList.add("CorporateLOS.EntityBasics");
				procList.add("CorporateLOS.EntityStatus");
				procList.add("CorporateLOS.AddressInfo");
				procList.add("CorporateLOS.ContactInfo");
				procList.add("CorporateLOS.FacilityStructureCheck");
				procList.add("CorporateLOS.FacilityPricingCheck");
				procList.add("CorporateLOS.BeneficialOwner");
				procList.add("CorporateLOS.PartySanctions");
				procList.add("CorporateLOS.ContactSanctions");
				procList.add("CorporateLOS.AnnualStatement");
				procList.add("CorporateLOS.BusinessEvidence");
			}
			if (modifiedFlag) {
				java.util.HashMap<String, String> partyamendRequestTypesMap = (java.util.HashMap<String, String>) kcontext
						.getVariable("amendRequestTypesMap");
				for (String partyId : partyamendRequestTypesMap.keySet()) {
                    System.out.println(partyId + " : " + kcontext.getVariable("relatedPartyId").toString());
					if (partyId.equals(kcontext.getVariable("relatedPartyId")
							.toString())) {
						procList.add("CorporateLOS.EntityBasics");
						procList.add("CorporateLOS.EntityStatus");
						procList.add("CorporateLOS.AddressInfo");
						procList.add("CorporateLOS.ContactInfo");
						procList.add("CorporateLOS.BeneficialOwner");
						procList.add("CorporateLOS.PartySanctions");
						procList.add("CorporateLOS.ContactSanctions");
						if (kcontext.getVariable("isIndividualParty")
								.toString().equals("false")) {
							procList.add("CorporateLOS.BusinessBasics");
						}
					}

				}

			}
			kcontext.setVariable("processesList", procList);
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		}
	}

	public static void checkFacilityPricing(
			org.kie.api.runtime.process.ProcessContext kcontext) {
		try {
			org.json.JSONObject responseObj = new org.json.JSONObject(kcontext
					.getVariable("response").toString());
			org.json.JSONArray pricingArr = responseObj.getJSONArray("pricing");
			if (pricingArr.length() > 0) {
				for (int i = 0; i < pricingArr.length(); i++) {
					org.json.JSONObject indPricingEle = new org.json.JSONObject(
							pricingArr.get(i).toString());
					if ((indPricingEle.get("pricingType").equals("01"))
							|| (indPricingEle.get("pricingType").equals("02"))
							|| (indPricingEle.get("pricingType").equals("03"))) {
						if (!indPricingEle.isNull("interestSpread")
								&& !indPricingEle.isNull("indicativeBaseRate")
								&& !indPricingEle.isNull("paymentFrequency")
								&& !indPricingEle.isNull("repricingFrequency")) {
							kcontext.setVariable("result", true);
						} else {
							System.out
									.println("Some of the pricing details are null");
							kcontext.setVariable("result", false);
						}
					} else {
						System.out
								.println("Pricing Type is not Fixed, Floating or periodic");
						kcontext.setVariable("result", false);
					}
				}
			} else {
				kcontext.setVariable("result", false);
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		}
	}

	public static void checkBusinessBasics(
			org.kie.api.runtime.process.ProcessContext kcontext) {
		try {
			org.json.JSONObject partyResp = new org.json.JSONObject(kcontext
					.getVariable("response").toString());
			if (!partyResp.isNull("dateOfIncorporation")) {
				kcontext.setVariable("orgCheck", true);
			} else {
				kcontext.setVariable("orgCheck", false);
			}
			org.json.JSONArray identifierArray = partyResp
					.getJSONArray("partyIdentifiers");
			for (int i = 0; i < identifierArray.length(); i++) {
				org.json.JSONObject identifierEle = new org.json.JSONObject(
						identifierArray.get(i).toString());
				if (identifierEle.get("type").equals("COMPANY.REG.NO")
						&& !identifierEle.isNull("identifierNumber")) {
					kcontext.setVariable("identifierCheck", true);
					break;
				} else {
					kcontext.setVariable("identifierCheck", false);
				}
			}
			Boolean org = (Boolean) kcontext.getVariable("orgCheck");
			Boolean identifier = (Boolean) kcontext
					.getVariable("identifierCheck");
			if (org && identifier) {
				kcontext.setVariable("result", true);
			} else {
				kcontext.setVariable("result", false);
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		}
	}

	public static void checkBeneOwner(
			org.kie.api.runtime.process.ProcessContext kcontext) {
		try {
			org.json.JSONObject relationsResp = new org.json.JSONObject(
					kcontext.getVariable("response").toString());
			if (relationsResp.has("partyRelations")) {
				org.json.JSONArray relationsArr = relationsResp
						.getJSONArray("partyRelations");
				for (int i = 0; i < relationsArr.length(); i++) {
					org.json.JSONObject indRelation = new org.json.JSONObject(
							relationsArr.get(i).toString());
					if (!indRelation.isNull("ownershipPercentage")
							&& java.lang.Integer.parseInt(indRelation.get(
									"ownershipPercentage").toString()) >= 25) {
						kcontext.setVariable("result", true);
						break;
					} else {
						kcontext.setVariable("result", false);
					}
				}
			} else {
				kcontext.setVariable("result", false);
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		}
	}

	public static void checkCompanyInfo(
			org.kie.api.runtime.process.ProcessContext kcontext) {
		try {
			org.json.JSONObject partyResp = new org.json.JSONObject(kcontext
					.getVariable("response").toString());
			if (!partyResp.isNull("dateOfIncorporation")) {
				String dateOfIncorporation = partyResp.get(
						"dateOfIncorporation").toString();
				java.time.LocalDate companyDate = java.time.LocalDate
						.parse(dateOfIncorporation);
				System.out.println("Company Date: " + companyDate.toString());
				java.time.Period difference = java.time.Period.between(
						companyDate, java.time.LocalDate.now());
				if (difference.getYears() > 2) {
					kcontext.setVariable("result", true);
				} else {
					kcontext.setVariable("result", false);
				}
			} else {
				kcontext.setVariable("result", false);
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		}
	}

	public static void checkAnnualStatement(
			org.kie.api.runtime.process.ProcessContext kcontext) {
		try {
			org.json.JSONObject responseJSON = new org.json.JSONObject(kcontext
					.getVariable("response").toString());
			if (responseJSON.has("documentsList")) {
				org.json.JSONArray documentsArray = responseJSON
						.getJSONArray("documentsList");
				if (documentsArray.length() > 0) {
					for (int i = 0; i < documentsArray.length(); i++) {
						org.json.JSONObject indDocument = new org.json.JSONObject(
								documentsArray.get(i).toString());
						if (indDocument.get("documentType").equals("fin_stmnt")
								&& indDocument.get("category").equals(
										"finan_statement")) {
							kcontext.setVariable("result", true);
							break;
						} else {
							kcontext.setVariable("result", false);
						}
					}
				} else {
					kcontext.setVariable("result", false);
				}
			} else {
				kcontext.setVariable("result", false);
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		}
	}

	public static void checkBusinessEvidence(
			org.kie.api.runtime.process.ProcessContext kcontext) {
		try {
			org.json.JSONObject responseJSON = new org.json.JSONObject(kcontext
					.getVariable("response").toString());
			if (responseJSON.has("documentsList")) {
				org.json.JSONArray documentsArray = responseJSON
						.getJSONArray("documentsList");
				if (documentsArray.length() > 0) {
					for (int i = 0; i < documentsArray.length(); i++) {
						org.json.JSONObject indDocument = new org.json.JSONObject(
								documentsArray.get(i).toString());
						if (indDocument.get("documentType").equals("tax_id")
								&& indDocument.get("category").equals(
										"prf_of_busi")) {
							kcontext.setVariable("result", true);
							break;
						} else {
							kcontext.setVariable("result", false);
						}
					}
				} else {
					kcontext.setVariable("result", false);
				}
			} else {
				kcontext.setVariable("result", false);
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		}
	}

	public static void facilityTypeCheck(
			org.kie.api.runtime.process.ProcessContext kcontext) {
		try {
			System.out.println("response: "
					+ kcontext.getVariable("response").toString());
			org.json.JSONObject productObj = new org.json.JSONObject(kcontext
					.getVariable("response").toString());
			if (productObj.has("productGroups")) {
				org.json.JSONArray productsGroupsArr = productObj
						.getJSONArray("productGroups");
				System.out.println("ProductGroupArray: "
						+ productsGroupsArr.toString());
				org.json.JSONObject currentProductGroupsObj = new org.json.JSONObject(
						productsGroupsArr.get(0).toString());
				if (currentProductGroupsObj.has("products")) {
					org.json.JSONArray productsArr = currentProductGroupsObj
							.getJSONArray("products");
					org.json.JSONArray responseFacility = new org.json.JSONArray(
							kcontext.getVariable("facilitiesList").toString());
					org.json.JSONArray responseFacilityType = new org.json.JSONArray(
							kcontext.getVariable("facilitiesTypeList")
									.toString());
					java.util.ArrayList newFacilityList = new java.util.ArrayList();
					for (int k = 0; k < responseFacility.length(); k++) {
						for (int j = 0; j < productsArr.length(); j++) {
							org.json.JSONObject currentProductObj = new org.json.JSONObject(
									productsArr.get(j).toString());
							if (currentProductObj.get("productRef").toString()
									.equals(responseFacilityType.get(k))
									&& !currentProductObj.get("productRef")
											.toString()
											.equals("LETTER.OF.CREDIT")
									&& !currentProductObj.get("productRef")
											.toString()
											.equals("LETTER.OF.GUARANTEE")) {
								newFacilityList.add(responseFacility.get(k));
								break;
							}
						}
						kcontext.setVariable("facilitiesIdList",
								newFacilityList);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		}
	}
}