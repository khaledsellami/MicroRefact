
from Settings import Settings
import json
from Entities.MyMethod import MyMethod
from Entities.Class import Class
from Entities.Cluster import Cluster
import copy
import re



"""
Function to create a object of type MyMethod
"""

def create_MyMethod(method_dict):

    name = method_dict["name"]
    returnType = method_dict["returnDataType"][0]
    parametersDataType = method_dict["parametersDataType"]
    body = method_dict["body"].split("\n")
    annotations = method_dict["annotations"]
    exceptions = method_dict["exception"]
    method = MyMethod(name,returnType,parametersDataType,exceptions,body,annotations)
    
    return method



def extract_Info_AST(Clusters):
    temp_json_location = f'{Settings.DIRECTORY}/javaParser/output.json'
    with open(temp_json_location) as json_file:  # read the AST
        ast = json.load(json_file)  # load AST

        extends_to_add = []
        merge_of_clusters = []  # store cluster merge information. Array with a tuple (cluster1, cluster2)

        for i, cluster in enumerate(Clusters):
            for i, classe in reversed(sorted(cluster.getClasses().items())):
                '''
                Class Information Extraction Section
                '''
                classe.setConstructor(ast[classe.getFull_Name()]["constructor"])
                classe.setBegin(ast[classe.getFull_Name()]["begin"])
                classe.setEnd(ast[classe.getFull_Name()]["end"])
                classe.setIsInterface(ast[classe.getFull_Name()]["isInterface"])
                classe.setAnnotation(ast[classe.getFull_Name()]["annotations"])
                classe.setInstance_variables(list(ast[classe.getFull_Name()]["instance_variables"]))
                classe.setMethods(ast[classe.getFull_Name()]["myMethods"])
                classe.setImplements(ast[classe.getFull_Name()]["implementedTypes"])
                classe.setExtends(ast[classe.getFull_Name()]["extendedTypes"])
                classe.setImports(ast[classe.getFull_Name()]["imports"])
                classe.setMethodsInvocations(ast[classe.getFull_Name()]["methodInvocations"])

                for meth in ast[classe.getFull_Name()]["myMethods"].values():
                    method = create_MyMethod(meth)
                    classe.addMyMethods(method)

                extendedTypes = ast[classe.getFull_Name()]["extendedTypes"]  # get extends

                '''
                Extends Handling Section
                '''
                extends_to_add = extends_to_add + find_extends(ast, classe, cluster, classe, extendedTypes, Clusters)
                print("EXTENDS TO ADD " + str(extends_to_add))

                if len(merge_of_clusters) > 0:
                    raise Exception("Bad code division")

                for indexC, extend, cluster, classe in extends_to_add:
                    print("%s    %s     %s    %s" % (indexC, extend, cluster.getPathToDirectory(), classe.getFull_Name()))
                    extendCopy = copy.deepcopy(Clusters[indexC].getClasses()[extend])
                    extendCopy.setIsOriginal(False)
                    cluster.getClasses()[extend] = extendCopy
                    # Extends are special glues that must enter the dict instead of new classes
                    classe.addClassGlue(extendCopy)

        return ast

def find_extends(ast, classe, cluster_of_Class, triggered_class, list_of_extends, Clusters):
    extends_to_add = []
    for clusterIndex, cl in enumerate(Clusters):
        for extend in list_of_extends:  # iterate over extends
            extended = extend
            if "<" in extend:
                ext = extend.split("<")[0]
                for imp in ast[classe.getFull_Name()]["imports"]:
                    if re.search("\." + ext + '$', imp):
                        extended = imp
                        break

            if extended in cl.getClasses():
                if extended not in cluster_of_Class.getClasses():  # if the extended class does not belong to the cluster
                    # Test 1: Copy the class to the cluster
                    extends_to_add.append((clusterIndex, extended, cluster_of_Class, triggered_class))
                    classE = Clusters[clusterIndex].getClasses()[extended]
                    listE = ast[classE.getFull_Name()]["extendedTypes"]
                    extends_to_add = extends_to_add + find_extends(ast, classE, cluster_of_Class, triggered_class, listE, Clusters)
    return extends_to_add

def find_implements(ast, Clusters, Classes):
    for cluster in Clusters:
        addToCluster = []  # must be done this way to add classes to the cluster, otherwise RuntimeError: dictionary changed size during iteration
        for classe in cluster.getClasses().values():
            '''
            Implements Section
            '''
            implementedTypes = ast[classe.getFull_Name()]["implementedTypes"]  # fetch implemented interfaces
            for interface in implementedTypes:  # iterate over interfaces
                if interface not in cluster.getClasses() and interface in Classes:  # if the interface does not belong to the cluster
                    # then it is necessary to add
                    c = Classes[interface]
                    addToCluster.append(c)

        for c in addToCluster:
            cluster.setClass(c)
        
        
                

